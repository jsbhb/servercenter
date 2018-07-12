package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.component.ActivityComponent;
import com.zm.goods.bussiness.component.GoodsServiceUtil;
import com.zm.goods.bussiness.component.PriceComponent;
import com.zm.goods.bussiness.component.PublishThreadPool;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.feignclient.SupplierFeignClient;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PopularizeDict;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.PriceModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.bo.CategoryBO;
import com.zm.goods.pojo.bo.ItemCountBO;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.PageModule;
import com.zm.goods.pojo.vo.TimeLimitActive;
import com.zm.goods.pojo.vo.TimeLimitActiveData;
import com.zm.goods.processWarehouse.ProcessWarehouse;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.JSONUtil;
import com.zm.goods.utils.PinYin4JUtil;
import com.zm.goods.utils.lucene.AbstractLucene;
import com.zm.goods.utils.lucene.LuceneFactory;

@Service("goodsService")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GoodsServiceImpl implements GoodsService {

	private static final Integer PICTURE_TYPE = 0;

	private static final Integer COOK_BOOK_TYPE = 1;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	ProcessWarehouse processWarehouse;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Resource
	ActivityComponent activityComponent;

	@Resource
	PriceComponent priceComponent;

	@Resource
	PublishThreadPool publishThreadPool;

	@Override
	public Object listGoods(Map<String, Object> param, Integer centerId, Integer userId, boolean proportion) {
		String centerIdstr = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", centerIdstr);
		if (param.get("itemId") != null) {
			List<String> itemIdList = new ArrayList<String>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			itemIdList.add((String) param.get("itemId"));
			tempMap.put("centerId", centerIdstr);
			tempMap.put("list", itemIdList);
			List<String> goodsIdList = goodsMapper.getGoodsIdByItemId(tempMap);
			param.put("goodsId", goodsIdList.get(0));
		}

		List<GoodsItem> goodsList = goodsMapper.listGoods(param);

		List<String> idList = new ArrayList<String>();

		if (goodsList == null || goodsList.size() == 0) {
			return null;
		}

		for (GoodsItem item : goodsList) {
			idList.add(item.getGoodsId());
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);
		parameter.put("centerId", centerIdstr);
		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(parameter);
		GoodsServiceUtil.packageGoodsItem(goodsList, fileList, specsList, true);
		if (param.get("goodsId") != null && Constants.PREDETERMINE_PLAT_TYPE != centerId) {
			activityComponent.doPackCoupon(centerId, userId, goodsList);
		}
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		Map<Integer, List<GoodsItem>> tempMap = new HashMap<Integer, List<GoodsItem>>();
		List<GoodsItem> tempList = null;
		for (GoodsItem item : goodsList) {
			if (tempMap.get(item.getSupplierId()) == null) {
				tempList = new ArrayList<GoodsItem>();
				tempList.add(item);
				tempMap.put(item.getSupplierId(), tempList);
			} else {
				tempMap.get(item.getSupplierId()).add(item);
			}
		}
		for (Map.Entry<Integer, List<GoodsItem>> entry : tempMap.entrySet()) {
			Map<String, String> map = hashOperations.entries(Constants.POST_TAX + entry.getKey());
			if (map != null) {
				String post = map.get("post");
				String tax = map.get("tax");
				for (GoodsItem item : entry.getValue()) {
					try {
						item.setFreePost(Integer.valueOf(post == null ? "0" : post));
						item.setFreeTax(Integer.valueOf(tax == null ? "0" : tax));
					} catch (Exception e) {
						LogUtil.writeErrorLog("【数字转换出错】" + post + "," + tax);
					}
				}
			}
		}

		if (proportion) {// 推手需要获取返佣比例
			Map<String, Object> result = new HashMap<String, Object>();
			String goodsId = param.get("goodsId").toString();
			Map<String, String> temp = hashOperations.entries(Constants.GOODS_REBATE + goodsId);
			double rebateProportion = 0;
			if (temp != null) {
				rebateProportion = Double.valueOf(temp.get("third") == null ? "0" : temp.get("third"));
			}
			result.put(Constants.PROPORTION, rebateProportion);
			result.put(GOODS_LIST, goodsList);
			return result;
		} else {
			return goodsList;
		}
	}

	@Override
	public List<PriceContrast> listPriceContrast(Map<String, Object> param) {

		return goodsMapper.listPriceContrast(param);
	}

	@Override
	public List<GoodsFile> listGoodsCookFile(String goodsId) {

		List<String> idList = new ArrayList<String>();
		idList.add(goodsId);
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", COOK_BOOK_TYPE);

		return goodsMapper.listGoodsFile(parameter);
	}

	@Override
	public Map<String, Object> tradeGoodsDetail(String itemId, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();

		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("itemId", itemId);
		GoodsSpecs specs = goodsMapper.getGoodsSpecs(param);
		if (specs == null) {
			return null;
		}
		GoodsServiceUtil.getPriceInterval(specs, specs.getDiscount());

		List<String> idList = new ArrayList<String>();
		idList.add(specs.getGoodsId());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specs", specs);
		result.put("pic", fileList);

		return result;
	}

	@Override
	public Map<String, Object> listGoodsSpecs(List<String> list, Integer centerId, String source) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("source", source);
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsByItemId(param);
		if (specsList == null || specsList.size() == 0) {
			return null;
		}

		for (GoodsSpecs specs : specsList) {
			GoodsServiceUtil.getPriceInterval(specs, specs.getDiscount());
		}

		List<String> idList = new ArrayList<String>();
		for (GoodsSpecs model : specsList) {
			idList.add(model.getGoodsId());
		}

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("centerId", id);
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specsList", specsList);
		result.put("pic", fileList);

		return result;
	}

	@Override
	public ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, Integer supplierId, boolean vip,
			Integer centerId, Integer orderFlag, String couponIds, Integer userId) {

		ResultModel result = new ResultModel(true, "");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Tax, Double> taxMap = new HashMap<Tax, Double>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("supplierId", supplierId);
		param.put("list", list);
		int count = goodsMapper.countGoodsBySupplierIdAndItemId(param);
		if (count != list.size()) {
			return new ResultModel(false, ErrorCodeEnum.SUPPLIER_GOODS_ERROR.getErrorCode(),
					ErrorCodeEnum.SUPPLIER_GOODS_ERROR.getErrorMsg());
		}
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("orderFlag", orderFlag);
		GoodsSpecs specs = null;
		Double totalAmount = 0.0;
		Integer weight = 0;
		Map<String, GoodsSpecs> specsMap = new HashMap<String, GoodsSpecs>();

		for (OrderBussinessModel model : list) {
			param.put("itemId", model.getItemId());
			specs = goodsMapper.getGoodsSpecsForOrder(param);
			if (specs == null) {
				return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
						"itemId=" + model.getItemId() + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
			}
			weight += specs.getWeight() * model.getQuantity();
			Double amount = GoodsServiceUtil.judgeQuantityRange(vip, result, specs, model);
			if (!result.isSuccess()) {
				return new ResultModel(false, ErrorCodeEnum.OUT_OF_RANGE.getErrorCode(),
						ErrorCodeEnum.OUT_OF_RANGE.getErrorMsg());
			}
			if (Constants.O2O_ORDER.equals(orderFlag)) {
				Tax tax = goodsMapper.getTax(param);
				if (taxMap.get(tax) == null) {
					taxMap.put(tax, amount);
				} else {
					taxMap.put(tax, taxMap.get(tax) + amount);
				}
			}

			specsMap.put(model.getItemId(), specs);
		}

		totalAmount = priceComponent.calPrice(list, specsMap, couponIds, false, vip, centerId, result, userId);
		if (!result.isSuccess()) {
			return result;
		}
		map.put("tax", taxMap);

		map.put("weight", weight);
		map.put("totalAmount", totalAmount);
		result.setSuccess(true);
		result.setObj(map);
		return result;
	}

	@Override
	public Activity getActivity(Map<String, Object> param) {

		return goodsMapper.getActivity(param);
	}

	@Override
	public List<Layout> getModular(String page, Integer centerId, Integer pageType) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("page", page);
		param.put("pageType", pageType);
		return goodsMapper.listLayout(param);
	}

	@Override
	public void createTable(Integer centerId) {
		goodsMapper.createGoodsItem(centerId);
		goodsMapper.createGoods(centerId);
		goodsMapper.createGoodsFile(centerId);
		goodsMapper.createFirstCategory(centerId);
		goodsMapper.createSecondCategory(centerId);
		goodsMapper.createThirdCategory(centerId);
		goodsMapper.createGoodsPrice(centerId);
		goodsMapper.createLayout(centerId);
		goodsMapper.createPopularizeDict(centerId);
		goodsMapper.createPopularizeData(centerId);
		goodsMapper.createActivity(centerId);
		goodsMapper.createActivityData(centerId);
		goodsMapper.createPriceContrast(centerId);
	}

	@Override
	public List<PageModule> getModularData(Integer pageType, String page, Layout layout, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<PageModule> result = new ArrayList<PageModule>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("page", page);
		param.put("pageType", pageType);
		if (layout.getId() == null) {
			List<Layout> layoutList = goodsMapper.listLayout(param);
			if (layoutList != null && layoutList.size() > 0) {
				for (Layout temp : layoutList) {
					packageData(param, result, temp);
				}
			}
		} else {
			packageData(param, result, layout);
		}

		return result;
	}

	private void packageData(Map<String, Object> param, List<PageModule> result, Layout temp) {
		param.put("layoutId", temp.getId());
		if (Constants.ACTIVE_MODEL.equals(temp.getType())) {
			List<Activity> activityList = goodsMapper.listActivityByLayoutId(param);
			if (activityList == null) {
				return;
			}
			for (Activity activity : activityList) {
				if (Constants.ACTIVE_START.equals(activity.getStatus())
						|| Constants.ACTIVE_UNSTART.equals(activity.getStatus())) {
					param.put("activeId", activity.getId());
					activity.setCode(temp.getCode());
					result.add(new PageModule(activity, goodsMapper.listActiveData(param)));
					break;
				}
			}
		} else {
			PopularizeDict dict = goodsMapper.getDictByLayoutId(param);
			if (dict == null) {
				return;
			}
			param.put("dictId", dict.getId());
			dict.setCode(temp.getCode());
			result.add(new PageModule(dict, goodsMapper.listDictData(param)));
		}
	}

	@Override
	public void updateActiveStart(Integer centerId, Integer activeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("activeId", activeId);
		goodsMapper.updateActivitiesStart(param);

	}

	@Override
	public void updateActiveEnd(Integer centerId, Integer activeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("activeId", activeId);
		goodsMapper.updateActivitiesEnd(param);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getEndActive() {
		Map<String, Object> result = new HashMap<String, Object>();
		ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
		if (resultModel.isSuccess()) {
			List<Integer> list = (List<Integer>) resultModel.getObj();
			for (Integer centerId : list) {
				List<Integer> activeIdList = goodsMapper.listEndActiveId("_" + centerId);
				result.put(centerId + "", activeIdList);
			}
		}
		return result;
	}

	@Override
	// 最早模式中所有区域中心同时上架，现在已经弃用
	public void createGoodsLucene(Integer centerId) {
		// Map<String, Object> param = new HashMap<String, Object>();
		// List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
		// if (centerId == null) {
		// ResultModel resultModel =
		// userFeignClient.getCenterId(Constants.FIRST_VERSION);
		// if (resultModel.isSuccess()) {
		// List<Integer> list = (List<Integer>) resultModel.getObj();
		// for (Integer id : list) {
		// String centerIdstr = GoodsServiceUtil.judgeCenterId(id);
		// param.put("centerId", centerIdstr);
		// // renderLuceneModel(param, searchList, id);
		// }
		// }
		// } else {
		// String centerIdstr = GoodsServiceUtil.judgeCenterId(centerId);
		// param.put("centerId", centerIdstr);
		// // renderLuceneModel(param, searchList, centerId);
		// }
	}

	@SuppressWarnings("unchecked")
	private List<String> renderLuceneModel(Map<String, Object> param, List<GoodsSearch> searchList, Integer id,
			List<String> itemIdList) {
		if (param.get("list") == null) {
			throw new RuntimeException("没有获取到商品编号对应的GOODS_ID");
		}
		List<GoodsItem> itemList = goodsMapper.listGoodsForLucene(param);
		List<String> goodsIds = new ArrayList<String>();
		// 封装新建lucene索引的数据searchList
		createNewLucenIndex(param, searchList, itemList, goodsIds, itemIdList);
		// 更新lucene索引的tag
		List<String> totalGoodsId = (List<String>) param.get("list");
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.writerIndex(searchList);
		// List<GoodsSearch> updateindexList = updateLuceneIndex(totalGoodsId,
		// goodsIds, itemIdList, id);
		// if(updateindexList != null && updateindexList.size() > 0){
		// lucene.updateIndex(updateindexList);
		// }
		goodsMapper.updateGoodsUpShelves(param);
		totalGoodsId.removeAll(goodsIds);
		return totalGoodsId;
	}

	// 更新上架中goods的tag lucene索引
	private void updateLuceneIndex(List<String> updateTagList, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", GoodsServiceUtil.judgeCenterId(centerId));
		param.put("list", updateTagList);
		List<GoodsItem> itemList = goodsMapper.listGoodsForLuceneUpdateTag(param);
		if (itemList != null && itemList.size() > 0) {
			GoodsSearch search = null;
			StringBuilder sb = new StringBuilder();
			List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
			Map<String, Double> result = null;
			for (GoodsItem item : itemList) {
				sb.delete(0, sb.length());
				search = new GoodsSearch();
				search.setGoodsId(item.getGoodsId());
				search.setBrand(item.getBrand());
				search.setStatus(item.getStatus());
				search.setOrigin(item.getOrigin());
				search.setFirstCategory(item.getFirstCategory());
				search.setThirdCategory(item.getThirdCategory());
				search.setSecondCategory(item.getSecondCategory());
				search.setGoodsName(item.getCustomGoodsName());
				search.setPopular(item.getPopular());
				search.setType(item.getType());
				search.setCreateTime(item.getCreateTime());
				if (item.getGoodsSpecsList() != null) {
					result = GoodsServiceUtil.getMinPrice(item.getGoodsSpecsList());
					search.setPrice(result.get("realPrice"));
					for (GoodsSpecs specs : item.getGoodsSpecsList()) {
						if (specs.getTagList() != null) {
							for (GoodsTagEntity entity : specs.getTagList()) {
								sb.append(entity.getTagName() + ",");
							}
						}
					}
					if (sb.length() > 0) {
						search.setTag(sb.substring(0, sb.length() - 1));
					} else {
						search.setTag(sb.toString());
					}
				}
				searchList.add(search);
			}
			AbstractLucene lucene = LuceneFactory.get(centerId);
			lucene.updateIndex(searchList);
		}
	}

	// 封装需要更新lucene索引标签的商品
	@SuppressWarnings("unused")
	private List<GoodsSearch> updateLuceneIndex(List<String> totalGoodsId, List<String> goodsIds,
			List<String> itemIdList, Integer centerId) {
		totalGoodsId.removeAll(goodsIds);// 排除新上架的goods，剩下的需要更新标签
		if (totalGoodsId.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("centerId", GoodsServiceUtil.judgeCenterId(centerId));
			param.put("list", itemIdList);
			// 根据点击上架的itemid，获取该items的goods已经上架的item
			List<GoodsSpecs> specsList = goodsMapper.listItemTagForLuceneUpdate(param);
			if (specsList != null && specsList.size() > 0) {
				// 根据goodsId 封装标签字符串，最后需要和已经上架的标签字符串合并
				Map<String, StringBuilder> temp = new HashMap<String, StringBuilder>();
				// 存放需要更新tag的goodsId，根据goodsId获取原有已经上架的tag
				Set<String> idSet = new HashSet<String>();
				// 获取新增lucene索引的tag
				getNewTagForLuceneIndex(specsList, temp, idSet);
				// 获取原来已上架的tag
				getUpshelvTag(param, temp, idSet);
				if (temp.size() > 0) {
					GoodsSearch search = null;
					List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
					for (Map.Entry<String, StringBuilder> entry : temp.entrySet()) {
						search = new GoodsSearch();
						search.setGoodsId(entry.getKey());
						search.setTag(entry.getValue().substring(0, entry.getValue().length() - 1));
						searchList.add(search);
					}
					return searchList;
				}
			}
		}
		return null;
	}

	private void getUpshelvTag(Map<String, Object> param, Map<String, StringBuilder> temp, Set<String> idSet) {
		StringBuilder sb;
		if (idSet.size() > 0) {
			param.put("list", idSet);
			List<GoodsSpecs> upshelvSpecsList = goodsMapper.listItemUpshelvTagForLuceneUpdate(param);
			if (upshelvSpecsList != null && upshelvSpecsList.size() > 0) {
				for (GoodsSpecs specs : upshelvSpecsList) {
					if (specs.getTagList() != null && specs.getTagList().size() > 0) {
						if (temp.get(specs.getGoodsId()) == null) {
							sb = new StringBuilder();
							for (GoodsTagEntity tag : specs.getTagList()) {
								sb.append(tag.getTagName() + ",");
							}
							temp.put(specs.getGoodsId(), sb);
						} else {
							for (GoodsTagEntity tag : specs.getTagList()) {
								temp.get(specs.getGoodsId()).append(tag.getTagName() + ",");
							}
						}
					}
				}
			}
		}
	}

	private void getNewTagForLuceneIndex(List<GoodsSpecs> specsList, Map<String, StringBuilder> temp,
			Set<String> idSet) {
		StringBuilder sb = null;
		// 封装新增加的tag
		for (GoodsSpecs specs : specsList) {
			if (specs.getTagList() != null && specs.getTagList().size() > 0) {
				idSet.add(specs.getGoodsId());
				if (temp.get(specs.getGoodsId()) == null) {
					sb = new StringBuilder();
					for (GoodsTagEntity tag : specs.getTagList()) {
						sb.append(tag.getTagName() + ",");
					}
					temp.put(specs.getGoodsId(), sb);
				} else {
					for (GoodsTagEntity tag : specs.getTagList()) {
						temp.get(specs.getGoodsId()).append(tag.getTagName() + ",");
					}
				}
			}
		}
	}

	private void createNewLucenIndex(Map<String, Object> param, List<GoodsSearch> searchList, List<GoodsItem> itemList,
			List<String> goodsIds, List<String> itemIds) {
		GoodsSearch searchModel;
		List<GoodsSpecs> tempList;
		Map<String, GoodsSearch> temp = new HashMap<String, GoodsSearch>();
		Map<String, List<GoodsSpecs>> tempSpecs = new HashMap<String, List<GoodsSpecs>>();
		StringBuilder sb = new StringBuilder();
		if (itemList != null && itemList.size() > 0) {
			Map<String, Double> result = null;
			for (GoodsItem item : itemList) {
				searchModel = new GoodsSearch();
				searchModel.setGoodsId(item.getGoodsId());
				searchModel.setBrand(item.getBrand());
				searchModel.setStatus(item.getStatus());
				searchModel.setOrigin(item.getOrigin());
				searchModel.setFirstCategory(item.getFirstCategory());
				searchModel.setThirdCategory(item.getThirdCategory());
				searchModel.setSecondCategory(item.getSecondCategory());
				searchModel.setGoodsName(item.getCustomGoodsName());
				searchModel.setPopular(item.getPopular());
				searchModel.setType(item.getType());
				searchModel.setCreateTime(item.getCreateTime());
				goodsIds.add(item.getGoodsId());
				temp.put(item.getGoodsId(), searchModel);
				searchList.add(searchModel);
			}
			param.put("goodsIds", goodsIds);
			param.put("itemIds", itemIds);
			List<GoodsSpecs> specsList = goodsMapper.listSpecsForLucene(param);
			for (GoodsSpecs specs : specsList) {
				if (tempSpecs.get(specs.getGoodsId()) == null) {
					tempList = new ArrayList<GoodsSpecs>();
					tempList.add(specs);
					tempSpecs.put(specs.getGoodsId(), tempList);
				} else {
					tempSpecs.get(specs.getGoodsId()).add(specs);
				}
			}
			for (Map.Entry<String, GoodsSearch> entry : temp.entrySet()) {
				tempList = tempSpecs.get(entry.getKey());
				if (tempList != null && tempList.size() > 0) {
					result = GoodsServiceUtil.getMinPrice(tempList);
					sb.delete(0, sb.length());
					for (GoodsSpecs specs : tempList) {
						if (specs.getTagList() != null && specs.getTagList().size() > 0) {
							for (GoodsTagEntity entity : specs.getTagList()) {
								sb.append(entity.getTagName());
								sb.append(",");
							}
						}
					}
					entry.getValue().setPrice(result.get("realPrice"));
					if (sb.length() > 0) {
						entry.getValue().setTag(sb.substring(0, sb.length() - 1));
					} else {
						entry.getValue().setTag(sb.toString());
					}
				}
			}
		}
	}

	private final String GOODS_LIST = "goodsList";
	private final String PAGINATION = "pagination";

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination) {
		Map<String, Object> resultMap = new HashMap<String, Object>(16);
		Map<String, Object> luceneMap = new HashMap<String, Object>();

		try {
			luceneMap = LuceneFactory.get(searchModel.getCenterId()).search(searchModel, pagination, sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer total = (Integer) luceneMap.get(Constants.TOTAL);
		List<String> goodsIdList = (List<String>) luceneMap.get(Constants.ID_LIST);
		pagination.setTotalRows(total == null ? 0 : (long) total);
		if (goodsIdList != null && goodsIdList.size() > 0) {
			List<GoodsItem> goodsList = new ArrayList<GoodsItem>();
			// 设置高亮
			Map<String, GoodsSearch> highlighterModel = (Map<String, GoodsSearch>) luceneMap
					.get(Constants.HIGHLIGHTER_MODEL);
			Map<String, Object> searchParm = new HashMap<String, Object>();
			searchParm.put("list", goodsIdList);
			if (sortList != null && sortList.getSortList() != null && sortList.getSortList().size() > 0) {
				searchParm.put("sortList", sortList.getSortList());
			}
			String centerId = GoodsServiceUtil.judgeCenterId(searchModel.getCenterId());
			// searchParm.put("goodsIds", goodsIdList);
			searchParm.put("centerId", centerId);
			goodsList = goodsMapper.queryGoodsItem(searchParm);
			// goodsList = (List<GoodsItem>) listGoods(searchParm,
			// searchModel.getCenterId(), null, false);
			// if (highlighterModel != null && highlighterModel.size() > 0) {
			// for (GoodsItem model : goodsList) {
			// if (highlighterModel.get(model.getGoodsId()).getGoodsName() !=
			// null
			// &&
			// !"".equals(highlighterModel.get(model.getGoodsId()).getGoodsName()))
			// {
			//
			// model.setCustomGoodsName(highlighterModel.get(model.getGoodsId()).getGoodsName());
			// }
			// }
			//
			// }

			List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(searchParm);
			Map<String, List<GoodsSpecs>> temp = new HashMap<String, List<GoodsSpecs>>();
			List<GoodsSpecs> temList = null;
			for (GoodsSpecs specs : specsList) {
				if (temp.get(specs.getGoodsId()) == null) {
					temList = new ArrayList<GoodsSpecs>();
					temList.add(specs);
					temp.put(specs.getGoodsId(), temList);
				} else {
					temp.get(specs.getGoodsId()).add(specs);
				}
			}
			Set<String> specsSet = null;
			Map<String, Double> result = null;
			for (GoodsItem model : goodsList) {
				temList = temp.get(model.getGoodsId());
				model.setGoodsSpecsList(temList);
				result = GoodsServiceUtil.getMinPrice(temList);
				for (GoodsSpecs specs : temList) {
					if (model.getSpecsInfo() == null) {
						specsSet = new HashSet<>();
					} else {
						specsSet = model.getSpecsInfo();
					}
					String specsInfo = specs.getInfo();
					if (specsInfo != null && !"".equals(specsInfo.trim())) {
						try {
							Map<String, String> specsMap = JSONUtil.parse(specsInfo, Map.class);
							for (Map.Entry<String, String> entry : specsMap.entrySet()) {
								specsSet.add(entry.getValue());
							}
						} catch (Exception e) {
							LogUtil.writeErrorLog(
									"规格格式错误：itemId=" + specs.getItemId() + "***********specsInfo=" + specsInfo);
						}
					}
				}
				model.setSpecsInfo(specsSet);
				model.setPrice(result.get("price"));
				model.setRealPrice(result.get("realPrice"));
			}

			searchParm.put("type", PICTURE_TYPE);

			List<GoodsFile> fileList = goodsMapper.listGoodsFile(searchParm);
			Map<String, List<GoodsFile>> Filetemp = new HashMap<String, List<GoodsFile>>();
			List<GoodsFile> tempList = null;
			for (GoodsFile file : fileList) {
				if (Filetemp.get(file.getGoodsId()) != null) {
					continue;
				}
				tempList = new ArrayList<GoodsFile>();
				tempList.add(file);
				Filetemp.put(file.getGoodsId(), tempList);
			}

			for (GoodsItem model : goodsList) {
				model.setGoodsFileList(Filetemp.get(model.getGoodsId()));
				model.setHref("/" + model.getAccessPath() + "/" + model.getGoodsId() + ".html");
			}

			resultMap.put(GOODS_LIST, goodsList);
		}

		resultMap.put(PAGINATION, pagination.webListConverter());
		Object obj = luceneMap.get(Constants.BRAND);
		if (obj != null) {
			List<String> list = new ArrayList<>((Set<String>) obj);
			Map<String, List<Object>> brandMap = PinYin4JUtil.packDataByFirstCode(list, String.class, null);
			resultMap.put(Constants.BRAND_PY, brandMap);
		}
		resultMap.put(Constants.BRAND, luceneMap.get(Constants.BRAND));
		resultMap.put(Constants.ORIGIN, luceneMap.get(Constants.ORIGIN));

		return resultMap;
	}

	@Override
	public List<GoodsIndustryModel> loadIndexNavigation(Integer centerId) {
		String id = GoodsServiceUtil.judgeCenterId(centerId);

		return goodsMapper.queryGoodsCategory(id);
	}

	@Override
	public void stockBack(List<OrderBussinessModel> list, Integer orderFlag) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		param.put("orderFlag", orderFlag);
		goodsMapper.updateStockBack(param);
	}

	@Override
	public List<TimeLimitActive> getTimelimitGoods(Integer centerId) {
		String id = GoodsServiceUtil.judgeCenterId(centerId);
		List<TimeLimitActive> list = goodsMapper.listLimitTimeData(id);
		Map<String, Object> searchParm = new HashMap<String, Object>();
		if (list == null || list.size() == 0) {
			return null;
		}
		for (TimeLimitActive active : list) {
			if (active.getDataList() != null && active.getDataList().size() > 0) {
				List<String> idList = new ArrayList<String>();
				for (TimeLimitActiveData data : active.getDataList()) {
					idList.add(data.getGoodsId());
				}
				searchParm.put("list", idList);
				searchParm.put("centerId", id);
				List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(searchParm);
				Map<String, GoodsSpecs> temp = new HashMap<String, GoodsSpecs>();
				for (GoodsSpecs specs : specsList) {
					specs.infoFilter();
					temp.put(specs.getGoodsId(), specs);
				}
				for (TimeLimitActiveData data : active.getDataList()) {
					GoodsSpecs specs = temp.get(data.getGoodsId());
					if (specs != null) {
						if (specs.getPriceList() != null && specs.getPriceList().size() > 0) {
							Double discount = specs.getDiscount() == null ? 10.0 : specs.getDiscount();
							discount = CalculationUtils.div(discount, 10.0);
							data.setPrice(specs.getPriceList().get(0).getPrice());
							data.setRealPrice(CalculationUtils.mul(specs.getPriceList().get(0).getPrice(), discount));
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<GoodsItem> listSpecialGoods(Integer centerId, Integer type) {
		String id = GoodsServiceUtil.judgeCenterId(centerId);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", id);
		param.put("type", type);
		return goodsMapper.listSpecialGoods(param);
	}

	@Override
	public ResultModel stockJudge(List<OrderBussinessModel> list, Integer orderFlag, Integer supplierId) {
		supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);
		ResultModel resultModel = processWarehouse.processWarehouse(orderFlag, list);
		if (!resultModel.isSuccess()) {
			return resultModel;
		}
		return resultModel;
	}

	@Override
	public boolean updateThirdWarehouseStock(List<WarehouseStock> list) {
		goodsMapper.updateThirdWarehouseStock(list);
		return true;
	}

	@Override
	public boolean saveThirdGoods(List<ThirdWarehouseGoods> list) {
		goodsMapper.saveThirdGoods(list);
		return true;
	}

	@Override
	public Double getCostPrice(List<OrderBussinessModel> list) {
		List<PriceModel> priceList = goodsMapper.getCostPrice(list);
		Double totalAmount = 0.0;
		if (priceList != null) {
			for (PriceModel model : priceList) {
				for (OrderBussinessModel temp : list) {
					if (model.getItemId().equals(temp.getItemId())) {
						if (temp.getQuantity() >= model.getMin()
								&& (model.getMax() == null || temp.getQuantity() <= model.getMax())) {
							totalAmount = CalculationUtils.add(totalAmount,
									CalculationUtils.mul(temp.getQuantity(), model.getPrice()));
						}
					}
				}
			}
		}

		return totalAmount;
	}

	@Override
	public List<OrderBussinessModel> checkStock() {
		return goodsMapper.checkStock();
	}

	@Override
	public ResultModel upShelves(List<String> itemIdList, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String centerIdstr = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", centerIdstr);
		List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
		List<String> itemIdS = new ArrayList<String>();
		List<String> goodsIdList = null;
		if (itemIdList != null && itemIdList.size() > 0) {
			param.put("list", itemIdList);
			List<ItemCountBO> temp = goodsMapper.countItem(param);
			// 判断之前有没有同步到商城端，将没有同步的先同步
			if (temp == null || temp.size() == 0) {// 说明所有itemId都没有
				for (String str : itemIdList) {
					itemIdS.add(str);
				}
			} else {
				List<String> tempStrList = new ArrayList<String>();
				for (ItemCountBO model : temp) {
					tempStrList.add(model.getItemId().trim());
				}
				for (String str : itemIdList) {
					if (!tempStrList.contains(str.trim())) {
						itemIdS.add(str);
					}
				}
			}
			if (itemIdS.size() > 0) {
				syncgoods(itemIdS, centerId);
			}
			goodsIdList = goodsMapper.getGoodsIdByItemId(param);
			param.remove("list");
			if (goodsIdList != null && goodsIdList.size() > 0) {
				param.put("list", goodsIdList);
				// 判断有没有分类没上架的，进行上架
				// TODO 系统自动上架
				// List<CategoryBO> categoryList =
				// goodsMapper.listCategoryByGoodsIds(goodsIdList);
				// categoryStatusModify(categoryList, SHOW, centerIdstr);
			}
		}

		List<String> updateTagList = renderLuceneModel(param, searchList, centerId, itemIdList);
		param.put("list", itemIdList);
		goodsMapper.updateGoodsItemUpShelves(param);
		if (updateTagList != null && updateTagList.size() > 0) {// 更新标签
			updateLuceneIndex(updateTagList, centerId);
		}
		publishThreadPool.publish(itemIdList, centerId);// 发布商品
		return new ResultModel(true, "");
	}

	private static final Integer SHOW = 1;
	private static final Integer HIDE = 0;

	// 上下架时自动控制分类数据的上下架
	@SuppressWarnings("unused")
	private void categoryStatusModify(List<CategoryBO> categoryList, Integer status, String centerIdstr) {
		if (categoryList != null && categoryList.size() > 0) {
			Set<String> firstSet = new HashSet<>();
			Set<String> secondSet = new HashSet<>();
			Set<String> thirdSet = new HashSet<>();
			for (CategoryBO model : categoryList) {
				firstSet.add(model.getFirstId());
				secondSet.add(model.getSecondId());
				thirdSet.add(model.getThirdId());
			}
			Map<String, Object> param = new HashMap<String, Object>();
			if (SHOW == status) {
				param.put("status", SHOW);
				param.put("cstatus", HIDE);
				param.put("list", firstSet);
				goodsMapper.updateFirstCategory(param);
				param.put("list", secondSet);
				goodsMapper.updateSecondCategory(param);
				param.put("list", thirdSet);
				goodsMapper.updateThirdCategory(param);
			}
			if (HIDE == status) {
				param.put("status", HIDE);
				param.put("cstatus", SHOW);
				param.put("centerId", centerIdstr);
				param.put("set", firstSet);
				List<String> firstIdList = goodsMapper.listHideFirstCategory(param);
				if (firstIdList == null || firstIdList.size() == 0) {
					param.put("list", firstSet);
					goodsMapper.updateFirstCategory(param);
				} else {
					if (firstIdList.size() < firstSet.size()) {
						firstSet.removeAll(firstIdList);
						param.put("list", firstSet);
						goodsMapper.updateFirstCategory(param);
					}
				}
				param.put("set", secondSet);
				List<String> secondIdList = goodsMapper.listHideSecondCategory(param);
				if (secondIdList == null || secondIdList.size() == 0) {
					param.put("list", secondSet);
					goodsMapper.updateSecondCategory(param);
				} else {
					if (secondIdList.size() < secondSet.size()) {
						secondSet.removeAll(secondIdList);
						param.put("list", secondSet);
						goodsMapper.updateSecondCategory(param);
					}
				}
				param.put("set", thirdSet);
				List<String> thirdIdList = goodsMapper.listHideThirdCategory(param);
				if (thirdIdList == null || thirdIdList.size() == 0) {
					param.put("list", thirdSet);
					goodsMapper.updateThirdCategory(param);
				} else {
					if (thirdIdList.size() < thirdSet.size()) {
						thirdSet.removeAll(thirdIdList);
						param.put("list", thirdSet);
						goodsMapper.updateThirdCategory(param);
					}
				}
			}
		}

	}

	@Override
	public ResultModel downShelves(List<String> itemIdList, Integer centerId) {
		if (itemIdList == null || itemIdList.size() == 0) {
			return new ResultModel(false, "请传入itemId");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		String centerIdstr = GoodsServiceUtil.judgeCenterId(centerId);
		param.put("centerId", centerIdstr);
		param.put("list", itemIdList);
		List<String> goodsIdList = goodsMapper.getGoodsIdByItemId(param);
		if (goodsIdList == null || goodsIdList.size() == 0) {
			return new ResultModel(false, "没有该商品");
		}
		Set<String> goodsIdSet = new HashSet<String>(goodsIdList);// 去重
		goodsMapper.updateGoodsItemDownShelves(param);// 商品更新为下架状态
		List<String> downShelvesGoodsIdList = new ArrayList<String>();
		List<String> updateTagGoodsIdList = new ArrayList<String>();
		param.put("set", goodsIdSet);
		List<ItemCountBO> temp = goodsMapper.countUpShelvesStatus(param);
		if (temp == null || temp.size() == 0) {// 如果所有item已经下架，goods也下架，并删除索引
			for (String str : goodsIdSet) {
				downShelvesGoodsIdList.add(str);
			}
		} else {// 如果所有item已经下架，goods也下架，并删除索引
			List<String> tempStrList = new ArrayList<String>();
			for (ItemCountBO model : temp) {
				tempStrList.add(model.getItemId().trim());
			}
			for (String str : goodsIdSet) {
				if (!tempStrList.contains(str.trim())) {
					downShelvesGoodsIdList.add(str);
				} else {
					updateTagGoodsIdList.add(str);
				}
			}
		}
		if (downShelvesGoodsIdList != null && downShelvesGoodsIdList.size() > 0) {
			deleteLuceneAndDownShelves(downShelvesGoodsIdList, centerId);
			// 需要下架的分类
			// TODO 系统下架分类
			// List<CategoryBO> categoryList =
			// goodsMapper.listCategoryByGoodsIds(goodsIdList);
			// categoryStatusModify(categoryList, HIDE, centerIdstr);
		}
		if (updateTagGoodsIdList.size() > 0) {
			updateLuceneIndex(updateTagGoodsIdList, centerId);
		}
		publishThreadPool.delPublish(itemIdList, centerId);// 删除商品和重新发布商品
		return new ResultModel(true, "");
	}

	private void deleteLuceneAndDownShelves(List<String> goodsIdList, Integer id) {
		String centerId = GoodsServiceUtil.judgeCenterId(id);
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.deleteIndex(goodsIdList);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("list", goodsIdList);
		goodsMapper.updateGoodsDownShelves(param);
	}

	public ResultModel syncgoods(List<String> itemIdList, Integer id) {
		if (itemIdList != null && itemIdList.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			String centerId = GoodsServiceUtil.judgeCenterId(id);
			param.put("list", itemIdList);
			List<String> goodsIdList = goodsMapper.getGoodsIdByItemId(param);
			// List<String> firstIdList =
			// goodsMapper.listFirstCategory(goodsIdList);
			// List<String> secondIdList =
			// goodsMapper.listSecondCategory(goodsIdList);
			// List<String> thirdIdList =
			// goodsMapper.listThirdCategory(goodsIdList);
			param.put("centerId", centerId);
			param.put("goodsIdlist", goodsIdList);
			// param.put("firstIdlist", firstIdList);
			// param.put("secondIdlist", secondIdList);
			// param.put("thirdIdlist", thirdIdList);
			param.put("itemList", itemIdList);
			// goodsMapper.insertCenterFirstCategory(param);
			// goodsMapper.insertCenterSecondCategory(param);
			// goodsMapper.insertCenterThirdCategory(param);
			goodsMapper.insertCenterGoods(param);
			// goodsMapper.insertCenterGoodsFile(param);
			goodsMapper.insertCenterGoodsItem(param);
			if (Constants.PREDETERMINE_PLAT_TYPE.equals(id)) {
				goodsMapper.insert2BGoodsPrice(param);
			} else {
				goodsMapper.insertCenterGoodsPrice(param);
			}
		}
		return new ResultModel(true, "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel unDistribution(List<String> itemIdList) {
		if (itemIdList == null || itemIdList.size() == 0) {
			return new ResultModel(false, "请传入itemId");
		}
		goodsMapper.updateGoodsItemUnDistribution(itemIdList);
		ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
		if (resultModel.isSuccess()) {
			List<Integer> list = new ArrayList<Integer>();
			list = (List<Integer>) resultModel.getObj();
			list.add(Constants.PREDETERMINE_PLAT_TYPE);
			for (Integer centerId : list) {
				downShelves(itemIdList, centerId);
			}
			return new ResultModel(true, "");
		} else {
			return resultModel;
		}
	}

	@Override
	public ResultModel syncStock(List<String> itemIdList) {
		if (itemIdList != null && itemIdList.size() > 0) {
			List<OrderBussinessModel> list = goodsMapper.checkStockByItemIds(itemIdList);
			if (list != null && list.size() > 0) {
				Map<Integer, List<OrderBussinessModel>> param = new HashMap<Integer, List<OrderBussinessModel>>();
				List<OrderBussinessModel> temp = null;
				for (OrderBussinessModel model : list) {
					if (param.get(model.getSupplierId()) == null) {
						temp = new ArrayList<OrderBussinessModel>();
						temp.add(model);
						param.put(model.getSupplierId(), temp);
					} else {
						param.get(model.getSupplierId()).add(model);
					}
				}
				for (Map.Entry<Integer, List<OrderBussinessModel>> entry : param.entrySet()) {
					supplierFeignClient.checkStock(Constants.FIRST_VERSION, entry.getKey(), entry.getValue());
				}
			}
		}
		return new ResultModel(true, "同步完成");
	}

	@Override
	public ResultModel delButtjoinOrderStock(List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag) {
		ResultModel result = new ResultModel(true, "");
		GoodsSpecs specs = null;

		Double temp = 0.0;
		for (OrderBussinessModel model : list) {
			specs = goodsMapper.getGoodsSpecsForButtJoinOrder(model.getItemId());
			if (specs == null) {
				return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
						"itemId=" + model.getItemId() + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
			}
			Double amount = GoodsServiceUtil.judgeQuantityRange(false, result, specs, model);
			if (!result.isSuccess()) {
				return new ResultModel(false, ErrorCodeEnum.OUT_OF_RANGE.getErrorCode(),
						ErrorCodeEnum.OUT_OF_RANGE.getErrorMsg());
			}
			temp = CalculationUtils.mul(model.getItemPrice(), model.getQuantity());
			if (CalculationUtils.sub(temp, amount) < -3 || CalculationUtils.sub(temp, amount) > 3) {
				return new ResultModel(false, ErrorCodeEnum.RETAIL_PRICE_ERROR.getErrorCode(),
						ErrorCodeEnum.RETAIL_PRICE_ERROR.getErrorMsg());
			}
		}

		if (supplierId != null) {
			supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);
		}

		result = processWarehouse.processWarehouse(orderFlag, list);
		if (!result.isSuccess()) {
			return new ResultModel(false, ErrorCodeEnum.OUT_OF_STOCK.getErrorCode(),
					ErrorCodeEnum.OUT_OF_STOCK.getErrorMsg());
		}

		return result;
	}

	@Override
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(Set<String> set) {
		List<String> temp = new ArrayList<String>(set);
		List<GoodsConvert> list = goodsMapper.listSkuAndConversionByItemId(temp);
		Map<String, GoodsConvert> result = new HashMap<String, GoodsConvert>();
		if (list != null && list.size() > 0) {
			for (GoodsConvert model : list) {
				result.put(model.getItemId(), model);
			}
		}
		return result;
	}

	@Override
	public ResultModel calStock(List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag) {

		supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);

		return processWarehouse.processWarehouse(orderFlag, list);
	}

}
