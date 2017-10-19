package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PopularizeDict;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.vo.PageModule;
import com.zm.goods.utils.CalculationUtils;
import com.zm.goods.utils.CommonUtils;
import com.zm.goods.utils.JSONUtil;
import com.zm.goods.utils.lucene.AbstractLucene;
import com.zm.goods.utils.lucene.LuceneFactory;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	private static final Integer PICTURE_TYPE = 0;

	private static final Integer COOK_BOOK_TYPE = 1;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Override
	public List<GoodsItem> listGoods(Map<String, Object> param) {

		List<GoodsItem> goodsList = goodsMapper.listGoods(param);

		List<String> idList = new ArrayList<String>();

		for (GoodsItem item : goodsList) {
			idList.add(item.getGoodsId());
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);
		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);
		parameter.put("centerId", param.get("centerId"));
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(parameter);
		if (param.get("goodsId") != null) {
			packageGoodsItem(goodsList, fileList, specsList, true);
		} else {
			packageGoodsItem(goodsList, fileList, specsList, false);
		}

		return goodsList;
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

		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("itemId", itemId);
		GoodsSpecs specs = goodsMapper.getGoodsSpecs(param);
		if (specs == null) {
			return null;
		}
		getPriceInterval(specs);

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
	public Map<String, Object> listGoodsSpecs(List<String> list, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsByItemId(param);
		if (specsList == null || specsList.size() == 0) {
			return null;
		}

		for (GoodsSpecs specs : specsList) {
			getPriceInterval(specs);
		}

		List<String> idList = new ArrayList<String>();
		for (GoodsSpecs model : specsList) {
			idList.add(model.getGoodsId());
		}

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("centerId", param.get("centerId"));
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specsList", specsList);
		result.put("pic", fileList);

		return result;
	}

	@Override
	public ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, boolean delStock, boolean vip,
			Integer centerId, Integer orderFlag) {

		ResultModel result = new ResultModel();

		Map<String, Object> param = new HashMap<String, Object>();
		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("orderFlag", orderFlag);
		GoodsSpecs specs = null;
		Double totalAmount = 0.0;
		for (OrderBussinessModel model : list) {

			param.put("itemId", model.getItemId());
			specs = goodsMapper.getGoodsSpecs(param);
			getPriceInterval(specs);
			boolean calculation = false;
			for (GoodsPrice price : specs.getPriceList()) {
				boolean flag = model.getQuantity() >= price.getMin()
						&& (price.getMax() == null || model.getQuantity() <= price.getMax());
				if (flag) {
					if (model.getDeliveryPlace() != null) {
						if (model.getDeliveryPlace().equals(price.getDeliveryPlace())) {
							totalAmount += model.getQuantity() * (vip
									? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice());
							calculation = true;
						}
					} else {
						totalAmount += model.getQuantity()
								* (vip ? (price.getVipPrice() == null ? 0 : price.getVipPrice()) : price.getPrice());
						calculation = true;
					}
				}
			}
			if (!calculation) {
				totalAmount += model.getQuantity() * (vip ? specs.getVipMinPrice() : specs.getMinPrice());
			}
		}

		if (delStock) {
			// TODO 用线程池对每个itemID加锁？
		}

		result.setSuccess(true);
		result.setObj(totalAmount);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void packageGoodsItem(List<GoodsItem> goodsList, List<GoodsFile> fileList, List<GoodsSpecs> specsList,
			boolean flag) {

		Map<String, GoodsItem> goodsMap = new HashMap<String, GoodsItem>();
		if (goodsList == null || goodsList.size() == 0) {
			return;
		}

		for (GoodsItem item : goodsList) {
			goodsMap.put(item.getGoodsId(), item);
		}

		GoodsItem item = null;
		List<GoodsFile> tempFileList = null;
		List<GoodsSpecs> tempSpecsList = null;
		if (fileList != null && fileList.size() > 0) {
			for (GoodsFile file : fileList) {
				item = goodsMap.get(file.getGoodsId());
				if (item == null) {
					continue;
				}
				if (item.getGoodsFileList() == null) {
					tempFileList = new ArrayList<GoodsFile>();
					tempFileList.add(file);
					item.setGoodsFileList(tempFileList);
				} else {
					item.getGoodsFileList().add(file);
				}
			}
		}

		if (specsList != null && specsList.size() > 0) {
			Map<String, Object> temp = null;
			Set<String> tempSet = null;
			for (GoodsSpecs specs : specsList) {
				item = goodsMap.get(specs.getGoodsId());
				if (item == null) {
					continue;
				}
				if (flag) {
					if (item.getGoodsSpecsList() == null) {
						tempSpecsList = new ArrayList<GoodsSpecs>();
						tempSpecsList.add(specs);
						item.setGoodsSpecsList(tempSpecsList);
					} else {
						item.getGoodsSpecsList().add(specs);
					}
					getPriceInterval(specs);
				} else {
					temp = JSONUtil.parse(specs.getInfo(), Map.class);
					for (Map.Entry<String, Object> entry : temp.entrySet()) {
						if (item.getSpecsInfo() == null) {
							tempSet = new HashSet<String>();
							tempSet.add(entry.getKey());
							item.setSpecsInfo(tempSet);
						} else {
							item.getSpecsInfo().add(entry.getKey());
						}
					}
				}
			}
		}

	}

	@Override
	public Activity getActivity(Map<String, Object> param) {

		return goodsMapper.getActivity(param);
	}

	@Override
	public List<Layout> getModular(String page, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("page", page);
		return goodsMapper.listLayout(param);
	}

	@Override
	public void createTable(Integer centerId) {
		goodsMapper.createGoodsItem(centerId);
		goodsMapper.createGoodsFile(centerId);
		goodsMapper.createFirstCategory(centerId);
		goodsMapper.createSecondCategory(centerId);
		goodsMapper.createThirdCategory(centerId);
		goodsMapper.createBrand(centerId);
		goodsMapper.createGoodsSpecs(centerId);
		goodsMapper.createGoodsPrice(centerId);
		goodsMapper.createGoodsStock(centerId);
		goodsMapper.createLayout(centerId);
		goodsMapper.createPopularizeDict(centerId);
		goodsMapper.createPopularizeData(centerId);
		goodsMapper.createActivity(centerId);
		goodsMapper.createActivityData(centerId);
		goodsMapper.createPriceContrast(centerId);
	}

	@Override
	public List<PageModule> getModularData(String page, Layout layout, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<PageModule> result = new ArrayList<PageModule>();
		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("page", page);
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
			Activity activity = goodsMapper.getActivityByLayoutId(param);
			if (activity == null) {
				return;
			}
			param.put("activeId", activity.getId());
			activity.setCode(temp.getCode());
			result.add(new PageModule(activity, goodsMapper.listActiveData(param)));
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
		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("activeId", activeId);
		goodsMapper.updateActivitiesStart(param);

	}

	@Override
	public void updateActiveEnd(Integer centerId, Integer activeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = judgeCenterId(centerId);
		param.put("centerId", id);
		param.put("activeId", activeId);
		goodsMapper.updateActivitiesEnd(param);

	}

	// 封装价格区间
	private void getPriceInterval(GoodsSpecs specs) {
		List<GoodsPrice> priceList = specs.getPriceList();
		if (priceList == null) {
			return;
		}
		Double discount = 10.0;
		if (Constants.PROMOTION.equals(specs.getPromotion())) {
			discount = specs.getDiscount();
		}
		discount = CalculationUtils.div(discount, 10.0);
		for (int i = 0; i < priceList.size(); i++) {
			if (i == 0) {
				specs.setMinPrice(CalculationUtils.mul(priceList.get(i).getPrice(), discount));
				specs.setMaxPrice(CalculationUtils.mul(priceList.get(i).getPrice(), discount));
				if (priceList.get(i).getVipPrice() != null) {
					specs.setVipMinPrice(CalculationUtils.mul(priceList.get(i).getVipPrice(), discount));
					specs.setVipMaxPrice(CalculationUtils.mul(priceList.get(i).getVipPrice(), discount));
				}

			} else {
				specs.setMinPrice(CalculationUtils
						.mul(CommonUtils.getMinDouble(specs.getMinPrice(), priceList.get(i).getPrice()), discount));
				specs.setMaxPrice(CalculationUtils
						.mul(CommonUtils.getMaxDouble(specs.getMaxPrice(), priceList.get(i).getPrice()), discount));
				if (priceList.get(i).getVipPrice() != null) {
					specs.setVipMinPrice(CalculationUtils.mul(
							CommonUtils.getMinDouble(specs.getVipMinPrice(), priceList.get(i).getVipPrice()),
							discount));
					specs.setVipMaxPrice(CalculationUtils.mul(
							CommonUtils.getMaxDouble(specs.getVipMaxPrice(), priceList.get(i).getVipPrice()),
							discount));
				}
			}
		}
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

	@SuppressWarnings("unchecked")
	@Override
	public void createGoodsLucene(Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
		if (centerId == null) {
			ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
			if (resultModel.isSuccess()) {
				List<Integer> list = (List<Integer>) resultModel.getObj();
				for (Integer id : list) {
					renderLuceneModel(param, searchList, id);
				}
			}
		} else {
			renderLuceneModel(param, searchList, centerId);
		}
	}

	private void renderLuceneModel(Map<String, Object> param, List<GoodsSearch> searchList, Integer id) {
		GoodsSearch searchModel;
		String centerId = judgeCenterId(id);
		List<GoodsItem> itemList = goodsMapper.listGoodsForLucene(centerId);
		for (GoodsItem item : itemList) {
			searchModel = new GoodsSearch();
			searchModel.setGoodsId(item.getGoodsId());
			searchModel.setBrand(item.getBrand());
			searchModel.setStatus(item.getStatus());
			searchModel.setOrigin(item.getOrigin());
			searchModel.setGoodsName(item.getCustomGoodsName());
			param.put("goodsId", item.getGoodsId());
			param.put("centerId", centerId);
			List<GoodsSpecs> specsList = goodsMapper.listSpecsForLucene(param);
			if (specsList != null && specsList.size() > 0) {
				searchModel.setPrice(specsList.get(0).getMinPrice());
			}
			searchList.add(searchModel);
		}
		AbstractLucene lucene = LuceneFactory
				.get((id == Constants.BIG_TRADE_CENTERID || id == Constants.O2O_CENTERID) ? 1 : id);
		lucene.writerIndex(searchList);
		goodsMapper.updateLuceneStatus(centerId);
	}

	private String judgeCenterId(Integer id) {
		String centerId;
		if (Constants.BIG_TRADE_CENTERID.equals(id) || Constants.O2O_CENTERID.equals(id)) {
			centerId = "";
		} else {
			centerId = "_" + id;
		}
		return centerId;
	}

	private final String GOODS_LIST = "goodsList";
	private final String PAGINATION = "pagination";

	@SuppressWarnings("unchecked")
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
		pagination.setTotalRows((long) total);
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
			String centerId = judgeCenterId(searchModel.getCenterId());

			searchParm.put("centerId", centerId);
			goodsList = goodsMapper.queryGoodsItem(searchParm);
			if (highlighterModel != null && highlighterModel.size() > 0) {
				for (GoodsItem model : goodsList) {
					if (highlighterModel.get(model.getGoodsId()).getGoodsName() != null
							&& !"".equals(highlighterModel.get(model.getGoodsId()).getGoodsName())) {

						model.setCustomGoodsName(highlighterModel.get(model.getGoodsId()).getGoodsName());
					}
				}

			}

			List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecs(searchParm);
			Map<String, GoodsSpecs> temp = new HashMap<String, GoodsSpecs>();
			for (GoodsSpecs specs : specsList) {
				temp.put(specs.getGoodsId(), specs);
			}
			for (GoodsItem model : goodsList) {
				GoodsSpecs specs = temp.get(model.getGoodsId());
				if (specs != null) {
					if (specs.getPriceList() != null && specs.getPriceList().size() > 0) {
						Double discount = 0.0;
						if (Constants.PROMOTION.equals(specs.getPromotion())) {
							discount = CalculationUtils.div(specs.getDiscount(), 10.0);
							model.setPrice(specs.getPriceList().get(0).getPrice());
							model.setRealPrice(CalculationUtils.mul(specs.getPriceList().get(0).getPrice(), discount));
						} else {
							model.setPrice(specs.getPriceList().get(0).getPrice());
							model.setRealPrice(specs.getPriceList().get(0).getPrice());
						}
					}
				}
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
			}

			resultMap.put(GOODS_LIST, goodsList);
		}

		resultMap.put(PAGINATION, pagination.webListConverter());
		resultMap.put(Constants.BRAND, luceneMap.get(Constants.BRAND));
		resultMap.put(Constants.ORIGIN, luceneMap.get(Constants.ORIGIN));

		return resultMap;
	}

}
