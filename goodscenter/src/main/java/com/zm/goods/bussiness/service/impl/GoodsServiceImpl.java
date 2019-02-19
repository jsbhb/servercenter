package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.annotation.GoodsLifeCycle;
import com.zm.goods.bussiness.component.GoodsServiceComponent;
import com.zm.goods.bussiness.component.PriceComponent;
import com.zm.goods.bussiness.component.ThreadPoolComponent;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.TpGoodsDisplay;
import com.zm.goods.enummodel.TradePatternEnum;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.factory.ViewObjectFactory;
import com.zm.goods.feignclient.SupplierFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.bo.CategoryBO;
import com.zm.goods.pojo.bo.ItemCountBO;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.GoodsItem;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.GoodsSpecsVO;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.processWarehouse.model.WarehouseModel;
import com.zm.goods.utils.JSONUtil;
import com.zm.goods.utils.PinYin4JUtil;
import com.zm.goods.utils.lucene.AbstractLucene;
import com.zm.goods.utils.lucene.LuceneFactory;

@Service("goodsService")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GoodsServiceImpl implements GoodsService {

	private final Integer PICTURE_TYPE = 0;

	private final Integer COOK_BOOK_TYPE = 1;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Resource
	PriceComponent priceComponent;

	@Resource
	GoodsServiceComponent goodsServiceComponent;

	@Resource
	ThreadPoolComponent threadPoolComponent;

	@SuppressWarnings("unchecked")
	@Override
	public GoodsVO listGoods(String goodsId, String specsTpId, boolean isApplet) {
		if (goodsId == null) {
			goodsId = goodsMapper.getGoodsIdBySpecsTpId(specsTpId);
		}

		GoodsItem goods = goodsMapper.getGoodsByGoodsId(goodsId);// 获取Goods
		List<GoodsSpecsTradePattern> goodsSpecsTpList = goodsMapper.listGoodsSpecsTpByGoodsId(goodsId);// 获取商品
		List<GoodsSpecs> specsList = new ArrayList<>();
		if (goodsSpecsTpList.size() > 0) {// 获取规格
			List<String> specsIdList = goodsSpecsTpList.stream().map(tp -> tp.getSpecsId())
					.collect(Collectors.toList());
			specsList = goodsMapper.listGoodsSpecsBySpecsIds(specsIdList);
		}
		// 获取库存
		TradePatternEnum tpe = TradePatternEnum.values()[goods.getType()];
		// 构造前端显示对象
		GoodsVO vo = packGoodsVO(goods, goodsSpecsTpList, specsList, tpe);
		// 初始化数据
		vo.init();
		if (isApplet) {
			goodsServiceComponent.packDetailPath(goods);
		}
		String bigsaleJson = (String) template.opsForValue().get(Constants.BIG_SALES_PRE);
		List<String> bigSaleList = new ArrayList<>();
		if (bigsaleJson != null) {
			bigSaleList = JSONUtil.parse(bigsaleJson, List.class);
			if (bigSaleList.contains(vo.getGoodsId())) {
				vo.setBigSale(true);
			}
		}
		return vo;
	}

	private GoodsVO packGoodsVO(GoodsItem goods, List<GoodsSpecsTradePattern> goodsSpecsTpList,
			List<GoodsSpecs> specsList, TradePatternEnum tpe) {
		List<WarehouseModel> stockList = null;
		switch (tpe) {
		case CROSS_BORDER:// 跨境
			if (goodsSpecsTpList.size() > 0) {
				List<String> itemIds = goodsSpecsTpList.stream().map(tp -> tp.getItemId()).collect(Collectors.toList());
				stockList = goodsMapper.listWarehouse(itemIds);
			}
			break;
		case GENERAL_TRADE:// 一般贸易
			if (goodsSpecsTpList.size() > 0) {
				List<String> specsTpIds = goodsSpecsTpList.stream().map(tp -> tp.getSpecsTpId())
						.collect(Collectors.toList());
				stockList = goodsMapper.listWarehouseBySpecsTpIds(specsTpIds);
			}
			break;
		}
		GoodsVO vo = ViewObjectFactory.createGoodsVO(goods, goodsSpecsTpList, specsList,
				Optional.ofNullable(stockList));
		return vo;
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
	public Map<String, Object> listGoodsSpecs(List<String> list, int platformSource, int gradeId)
			throws WrongPlatformSource {
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsTpBySpecsTpIds(list);
		if (specsList == null || specsList.size() == 0) {
			return null;
		}
		// 设置价格
		switch (platformSource) {
		case Constants.WELFARE_WEBSITE:
			getWelfareWebsitePriceInterval(specsList, gradeId);
			break;
		case Constants.BACK_MANAGER_WEBSITE:
			getBackWebsitePriceInterval(specsList, gradeId);
			break;
		default:
			getPriceInterval(specsList);
			break;
		}

		List<WarehouseModel> stockList = goodsMapper.listWarehouse(list);
		// 设置库存
		for (GoodsSpecs specs : specsList) {
			for (WarehouseModel model : stockList) {
				if (specs.getItemId().equals(model.getItemId())) {
					specs.setStock(model.getFxqty());
					break;
				}
			}
		}

		// 设置图片
		List<String> idList = new ArrayList<String>();
		for (GoodsSpecs model : specsList) {
			idList.add(model.getGoodsId());
		}

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("list", idList);
		parameter.put("type", PICTURE_TYPE);

		List<GoodsFile> fileList = goodsMapper.listGoodsFile(parameter);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("specsList", specsList);
		result.put("pic", fileList);

		return result;
	}

	private void getBackWebsitePriceInterval(List<GoodsSpecs> specsList, int gradeId) {
		for (GoodsSpecs specs : specsList) {
			goodsServiceComponent.getBackWebsitePriceInterval(specs, specs.getDiscount(), gradeId);
		}
	}

	private void getWelfareWebsitePriceInterval(List<GoodsSpecsTradePattern> goodsSpecsTpList, int gradeId) throws WrongPlatformSource {
		for (GoodsSpecsTradePattern specs : goodsSpecsTpList) {
			goodsServiceComponent.getWelfareWebsitePriceInterval(specs, specs.getDiscount(), gradeId);
		}
	}

	private void getPriceInterval(List<GoodsSpecs> specsList) {
		for (GoodsSpecs specs : specsList) {
			goodsServiceComponent.getPriceInterval(specs, specs.getDiscount());
		}
	}

	@SuppressWarnings("unchecked")
	private void renderLuceneModel(Integer id, List<String> specsTpIdList) {
		// 获取需要建索引的数据
		List<GoodsSearch> indexList = goodsMapper.listSpecsNeedToCreateIndex(specsTpIdList);
		// 创建索引
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.writerIndex(indexList);
	}

	// 更新上架中goods的tag lucene索引
	@Override
	public void updateLuceneIndex(List<String> updateTagList, Integer centerId) {
		List<GoodsItem> itemList = goodsMapper.listGoodsForLuceneUpdateTag(updateTagList);
		if (itemList != null && itemList.size() > 0) {
			GoodsSearch search = null;
			StringBuilder sb = new StringBuilder();
			List<GoodsSearch> searchList = new ArrayList<GoodsSearch>();
			Map<String, Double> result = null;
			for (GoodsItem item : itemList) {
				boolean isFx = false;
				sb.delete(0, sb.length());
				search = new GoodsSearch();
				LucenceModelConvertor.convertToGoodsSearch(item, search);
				if (item.getGoodsSpecsList() != null) {
					result = goodsServiceComponent.getMinPrice(item.getGoodsSpecsList(), false);
					search.setPrice(result.get("realPrice"));
					for (GoodsSpecs specs : item.getGoodsSpecsList()) {
						if (specs.getFx() == CAN_BE_FX) {// 有一个可以分销的就要做进lucene
							isFx = true;
						}
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
					if (isFx) {
						search.setFx(CAN_BE_FX);
					} else {
						search.setFx(CAN_NOT_BE_FX);
					}
				}
				searchList.add(search);
			}
			AbstractLucene lucene = LuceneFactory.get(centerId);
			lucene.updateIndex(searchList);
		}
	}

	private final String GOODS_LIST = "goodsList";
	private final String PAGINATION = "pagination";

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryGoods(GoodsSearch searchModel, SortModelList sortList, Pagination pagination,
			int gradeId, boolean welfare) throws WrongPlatformSource {
		Map<String, Object> resultMap = new HashMap<String, Object>(16);
		Map<String, Object> luceneMap = new HashMap<String, Object>();

		try {
			luceneMap = LuceneFactory.get(searchModel.getCenterId()).search(searchModel, pagination, sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer total = (Integer) luceneMap.get(Constants.TOTAL);
		List<String> specsTpIdList = (List<String>) luceneMap.get(Constants.ID_LIST);
		pagination.setTotalRows(total == null ? 0 : (long) total);
		if (specsTpIdList != null && specsTpIdList.size() > 0) {// 根据lucene搜索到的ID查询数据库
			Map<String, Object> searchParm = new HashMap<String, Object>();
			searchParm.put("list", specsTpIdList);
			if (sortList != null && sortList.getSortList() != null && sortList.getSortList().size() > 0) {
				searchParm.put("sortList", sortList.getSortList());
			}
			searchParm.put("isWelfare", welfare ? 1 : 0);
			List<GoodsSpecsTradePattern> goodsSpecsTpList = goodsMapper.listGoodsSpecsTpForFrontSearch(searchParm);// 获取商品
			if (goodsSpecsTpList == null) {
				return null;
			}
			// 获取goods
			List<String> goodsIds = goodsSpecsTpList.stream().map(tp -> tp.getGoodsId()).collect(Collectors.toList());
			List<GoodsItem> goodsList = goodsMapper.listGoodsItemByGoodsIds(goodsIds);
			// 获取specs
			List<String> specsIds = goodsSpecsTpList.stream().map(tp -> tp.getSpecsId()).collect(Collectors.toList());
			List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsBySpecsIds(specsIds);

			if (welfare) {
				getWelfareWebsitePriceInterval(goodsSpecsTpList, gradeId);
			}
			List<GoodsVO> goodsVOList = new ArrayList<>();
			List<GoodsSpecsTradePattern> tmpList = new ArrayList<>();
			Map<String, GoodsItem> goodsMap = goodsList.stream()
					.collect(Collectors.toMap(GoodsItem::getGoodsId, GoodsItem -> GoodsItem));
			goodsSpecsTpList.stream().forEach(specsTp -> {
				tmpList.clear();
				tmpList.add(specsTp);
				goodsVOList.add(
						ViewObjectFactory.createGoodsVO(goodsMap.get(specsTp.getGoodsId()), tmpList, specsList, null));
			});

			for (GoodsVO vo : goodsVOList) {
				vo.setHref("/" + vo.getAccessPath() + "/" + vo.getGoodsId() + ".html");
			}

			resultMap.put(GOODS_LIST, goodsVOList);
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
		return goodsMapper.queryGoodsCategory();
	}

	@Override
	@GoodsLifeCycle(status = 1, isFx = 1, remark = "商品上架")
	public ResultModel tradeGoodsUpShelves(List<String> specsTpIdList, Integer centerId, int display) {
		if (specsTpIdList != null && specsTpIdList.size() > 0) {
			// 状态更新为上架
			goodsMapper.updateTradeSpecsUpshelf(specsTpIdList, display);
			// 只有前端展示的时候才需要建lucene索引
			if (TpGoodsDisplay.FRONT.ordinal() == display || TpGoodsDisplay.BOTH.ordinal() == display) {
				// 新上架创建索引
				renderLuceneModel(centerId, specsTpIdList);
			}
			threadPoolComponent.publish(specsTpIdList, centerId);// 发布商品
			// FIXME
			// threadPoolComponent.sendGoodsInfo(specsTpIdList);// 通知对接用户商品上架
			return new ResultModel(true, "");
		} else {
			return new ResultModel(false, "没有提供上架商品信息");
		}
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
	@GoodsLifeCycle(status = 0, isFx = 0, remark = "商品下架")
	public ResultModel downShelves(List<String> itemIdList, Integer centerId) {
		if (itemIdList == null || itemIdList.size() == 0) {
			return new ResultModel(false, "请传入itemId");
		}
		List<String> goodsIdList = goodsMapper.getGoodsIdByItemId(itemIdList);
		if (goodsIdList == null || goodsIdList.size() == 0) {
			return new ResultModel(false, "没有该商品");
		}
		Set<String> goodsIdSet = new HashSet<String>(goodsIdList);// 去重
		goodsMapper.updateGoodsItemDownShelves(itemIdList);// 商品更新为下架状态,同时不可分销

		// 获取所有规格下架的goodsId和部分规格下架的goodsId
		List<String> downShelvesGoodsIdList = new ArrayList<String>();
		List<String> updateTagGoodsIdList = new ArrayList<String>();
		getAllAndSectionSpecsDownShelves(goodsIdSet, downShelvesGoodsIdList, updateTagGoodsIdList);

		// lucene下架商品，并更新整个goods为下架状态
		if (downShelvesGoodsIdList != null && downShelvesGoodsIdList.size() > 0) {
			deleteLuceneAndDownShelves(downShelvesGoodsIdList, centerId);
			// 该部分为系统根据分类下是否还有上架的商品自动进行分类的显示和隐藏
			// List<CategoryBO> categoryList =
			// goodsMapper.listCategoryByGoodsIds(goodsIdList);
			// categoryStatusModify(categoryList, HIDE, centerIdstr);
		}
		// 更新lucene索引
		if (updateTagGoodsIdList.size() > 0) {
			updateLuceneIndex(updateTagGoodsIdList, centerId);
		}
		threadPoolComponent.delPublish(itemIdList, centerId);// 删除商品和重新发布商品
		threadPoolComponent.sendGoodsInfoDownShelves(itemIdList);// 通知对接用户商品下架
		return new ResultModel(true, "");
	}

	private void getAllAndSectionSpecsDownShelves(Set<String> goodsIdSet, List<String> downShelvesGoodsIdList,
			List<String> updateTagGoodsIdList) {
		List<String> goodsIdList = new ArrayList<String>(goodsIdSet);
		List<ItemCountBO> temp = goodsMapper.countUpShelvesStatus(goodsIdList);
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
	}

	private void deleteLuceneAndDownShelves(List<String> goodsIdList, Integer id) {
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.deleteIndex(goodsIdList);
		goodsMapper.updateGoodsDownShelves(goodsIdList);
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
	public List<GoodsItem> listGoodsByGoodsIds(List<String> goodsIdList) {

		return goodsMapper.listGoodsByGoodsIds(goodsIdList);
	}

}
