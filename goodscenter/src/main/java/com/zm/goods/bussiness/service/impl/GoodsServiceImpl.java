package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.goods.bussiness.component.GoodsServiceComponent;
import com.zm.goods.bussiness.component.ThreadPoolComponent;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.AutoSelectionModeEnum;
import com.zm.goods.enummodel.TpGoodsDisplayEnum;
import com.zm.goods.enummodel.TradePatternEnum;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.factory.ViewObjectFactory;
import com.zm.goods.feignclient.SupplierFeignClient;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.bo.AutoSelectionBO;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsPricePO;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.GoodsSpecsVO;
import com.zm.goods.pojo.vo.GoodsVO;
import com.zm.goods.pojo.vo.SpecsTpStockVO;
import com.zm.goods.processWarehouse.model.WarehouseModel;
import com.zm.goods.utils.JSONUtil;
import com.zm.goods.utils.lucene.AbstractLucene;
import com.zm.goods.utils.lucene.LuceneFactory;

@Service("goodsService")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GoodsServiceImpl implements GoodsService {

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Resource
	GoodsServiceComponent goodsServiceComponent;

	@Resource
	ThreadPoolComponent threadPoolComponent;

	@Override
	public List<SpecsTpStockVO> getGoodsStock(String goodsId, Integer centerId) {
		int type = goodsMapper.getGoodsTypeByGoodsId(goodsId);
		List<GoodsSpecsTradePattern> goodsSpecsTpList = goodsMapper.listGoodsSpecsTpByGoodsId(goodsId);// 获取商品
		List<WarehouseModel> stockList = packStock(type, goodsSpecsTpList);
		final List<SpecsTpStockVO> voList = new ArrayList<>();
		if (stockList != null && stockList.size() > 0) {
			stockList.stream().forEach(stock -> {
				voList.add(ViewObjectFactory.createSpecsTpStockVO(stock));
			});
		}
		return voList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GoodsVO listGoods(String goodsId, String specsTpId, boolean isApplet) {
		if (goodsId == null) {
			goodsId = goodsMapper.getGoodsIdBySpecsTpId(specsTpId);
		}

		Goods goods = goodsMapper.getGoodsByGoodsId(goodsId);// 获取Goods
		List<GoodsSpecsTradePattern> goodsSpecsTpList = goodsMapper.listGoodsSpecsTpByGoodsId(goodsId);// 获取商品
		List<GoodsSpecs> specsList = new ArrayList<>();
		if (goodsSpecsTpList.size() > 0) {// 获取规格
			List<String> specsIdList = goodsSpecsTpList.stream().map(tp -> tp.getSpecsId())
					.collect(Collectors.toList());
			specsList = goodsMapper.listGoodsSpecsBySpecsIds(specsIdList);
		}
		// 获取库存
		List<WarehouseModel> stockList = packStock(goods.getType(), goodsSpecsTpList);
		// 构造前端显示对象
		GoodsVO vo = ViewObjectFactory.createGoodsVO(goods, goodsSpecsTpList, specsList,
				Optional.ofNullable(stockList));
		// 补全最小购买量
		completionMinBuyCount(goods.getType(), vo);
		// 跨境补全供应商信息
		if (TradePatternEnum.CROSS_BORDER.getType() == goods.getType()) {
			completionSupplier(vo);
		}
		// 初始化数据
		vo.init();
		if (isApplet) {
			goodsServiceComponent.packDetailPath(goods);
		}
		String bigsaleJson = (String) template.opsForValue().get(Constants.BIG_SALES_PRE);
		List<String> bigSaleList = new ArrayList<>();
		if (bigsaleJson != null) {
			bigSaleList = JSONUtil.parse(bigsaleJson, List.class);
			for (GoodsSpecsVO v : vo.getSpecsList()) {
				if (bigSaleList.contains(v.getSpecsTpId())) {
					v.setBigSale(true);
				}
			}
		}
		return vo;
	}

	/**
	 * @fun 补全最小购买量
	 * @param type
	 * @param vo
	 */
	private void completionMinBuyCount(int type, GoodsVO vo) {
		TradePatternEnum tpe = TradePatternEnum.valueof(type);
		List<GoodsPricePO> priceList = null;
		switch (tpe) {
		case CROSS_BORDER:
			List<String> itemIds = vo.getSpecsList().stream().map(tp -> tp.getItemId()).collect(Collectors.toList());
			priceList = goodsMapper.listGoodsPriceByItemIds(itemIds);
			Map<String, List<GoodsPricePO>> map = priceList.stream()
					.collect(Collectors.groupingBy(price -> price.getItemId()));
			vo.getSpecsList().forEach(specs -> {
				specs.setMinBuyCount(map.get(specs.getItemId()).stream()
						.sorted(Comparator.comparing(GoodsPricePO::getMin)).findFirst().get().getMin());
			});
			break;
		case GENERAL_TRADE:
			List<String> specsTpIds = vo.getSpecsList().stream().map(tp -> tp.getSpecsTpId())
					.collect(Collectors.toList());
			priceList = goodsMapper.listGoodsPriceBySpecsTpIds(specsTpIds);
			Map<String, List<GoodsPricePO>> tmap = priceList.stream()
					.collect(Collectors.groupingBy(price -> price.getSpecsTpId()));
			vo.getSpecsList().forEach(specs -> {
				specs.setMinBuyCount(tmap.get(specs.getSpecsTpId()).stream()
						.sorted(Comparator.comparing(GoodsPricePO::getMin)).findFirst().get().getMin());
			});
			break;
		default:
			break;
		}

	}

	/**
	 * @fun 补全供应商信息
	 * @param vo
	 */
	private void completionSupplier(GoodsVO vo) {
		List<String> itemIds = vo.getSpecsList().stream().map(tp -> tp.getItemId()).collect(Collectors.toList());
		List<Items> itemsList = goodsMapper.listItemsByItemIds(itemIds);
		Map<String, Items> itemsMap = itemsList.stream().collect(Collectors.toMap(Items::getItemId, items -> items));
		vo.getSpecsList().stream().forEach(tp -> {
			tp.setSupplierId(Optional.ofNullable(itemsMap.get(tp.getItemId())).orElse(new Items()).getSupplierId());
			tp.setSupplierName(Optional.ofNullable(itemsMap.get(tp.getItemId())).orElse(new Items()).getSupplierName());
		});
	}

	private List<WarehouseModel> packStock(int type, List<GoodsSpecsTradePattern> goodsSpecsTpList) {
		TradePatternEnum tpe = TradePatternEnum.valueof(type);
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
		return stockList;
	}

	@Override
	public List<GoodsVO> listGoodsSpecs(List<String> list, int platformSource, int gradeId) throws WrongPlatformSource {
		List<GoodsSpecsTradePattern> goodsSpecsTpList = goodsMapper.listGoodsSpecsTpBySpecsTpIds(list);
		if (goodsSpecsTpList == null || goodsSpecsTpList.size() == 0) {
			return null;
		}
		List<GoodsVO> goodsVOList = packGoodsVOBySpecsTp(goodsSpecsTpList);
		// 初始化
		goodsVOList.stream().forEach(vo -> {
			vo.init();
			if (TradePatternEnum.CROSS_BORDER.getType() == vo.getType()) {
				completionSupplier(vo);
			}
		});
		// 设置价格
		switch (platformSource) {
		case Constants.WELFARE_WEBSITE:
			getWelfareWebsitePriceInterval(goodsSpecsTpList, gradeId);
			break;
		case Constants.BACK_MANAGER_WEBSITE:
			getBackWebsitePriceInterval(goodsSpecsTpList, gradeId);
			break;
		default:
			break;
		}

		return goodsVOList;
	}

	/**
	 * @fun 根据GoodsSpecsTradePattern生成GoodsVO
	 * @param goodsSpecsTpList
	 * @return
	 */
	private List<GoodsVO> packGoodsVOBySpecsTp(List<GoodsSpecsTradePattern> goodsSpecsTpList) {
		// 获取goods
		List<String> goodsIds = goodsSpecsTpList.stream().map(tp -> tp.getGoodsId()).collect(Collectors.toList());
		List<Goods> goodsList = goodsMapper.listGoodsItemByGoodsIds(goodsIds);
		// 获取specs
		List<String> specsIds = goodsSpecsTpList.stream().map(tp -> tp.getSpecsId()).collect(Collectors.toList());
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsBySpecsIds(specsIds);
		List<GoodsVO> goodsVOList = new ArrayList<>();
		List<GoodsSpecsTradePattern> tmpList = new ArrayList<>();
		Map<String, Goods> goodsMap = goodsList.stream()
				.collect(Collectors.toMap(Goods::getGoodsId, GoodsItem -> GoodsItem));
		goodsSpecsTpList.stream().forEach(specsTp -> {
			tmpList.clear();
			tmpList.add(specsTp);
			goodsVOList.add(ViewObjectFactory.createGoodsVO(goodsMap.get(specsTp.getGoodsId()), tmpList, specsList,
					Optional.ofNullable(null)));
		});
		return goodsVOList;
	}

	private void getBackWebsitePriceInterval(List<GoodsSpecsTradePattern> goodsSpecsTpList, int gradeId) {
		for (GoodsSpecsTradePattern specs : goodsSpecsTpList) {
			goodsServiceComponent.getBackWebsitePriceInterval(specs, specs.getDiscount(), gradeId);
		}
	}

	private void getWelfareWebsitePriceInterval(List<GoodsSpecsTradePattern> goodsSpecsTpList, int gradeId)
			throws WrongPlatformSource {
		for (GoodsSpecsTradePattern specs : goodsSpecsTpList) {
			goodsServiceComponent.getWelfareWebsitePriceInterval(specs, specs.getDiscount(), gradeId);
		}
	}

	private void renderLuceneModel(Integer id, List<String> specsTpIdList) {
		// 获取需要建索引的数据
		List<GoodsSearch> indexList = goodsMapper.listSpecsNeedToCreateIndex(specsTpIdList);
		// 创建索引
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.writerIndex(indexList);
	}

	// 更新lucene索引
	@Override
	public void updateLuceneIndex(List<String> specsTpIdList, Integer centerId) {
		List<GoodsSearch> indexList = goodsMapper.listSpecsNeedToCreateIndex(specsTpIdList);
		AbstractLucene lucene = LuceneFactory.get(centerId);
		lucene.updateIndex(indexList);
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
			List<GoodsSpecsTradePattern> goodsSpecsTpList = goodsMapper.listGoodsSpecsTpForFrontSearch(searchParm);// 获取商品
			if (goodsSpecsTpList == null) {
				return null;
			}
			// 生成显示对象list
			List<GoodsVO> goodsVOList = packGoodsVOBySpecsTp(goodsSpecsTpList);
			// 初始化
			goodsVOList.stream().forEach(GoodsVO::init);
			if (welfare) {
				getWelfareWebsitePriceInterval(goodsSpecsTpList, gradeId);
			}

			for (GoodsVO vo : goodsVOList) {
				vo.setHref("/" + vo.getAccessPath() + "/" + vo.getGoodsId() + ".html");
			}

			resultMap.put(GOODS_LIST, goodsVOList);
		}

		resultMap.put(PAGINATION, pagination.webListConverter());
		for (Map.Entry<String, Object> entry : luceneMap.entrySet()) {
			if (!entry.getKey().equals(Constants.TOTAL) && !entry.getKey().equals(Constants.ID_LIST)) {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		return resultMap;
	}

	@Override
	public List<GoodsIndustryModel> loadIndexNavigation(Integer centerId) {
		return goodsMapper.queryGoodsCategory();
	}

	@Override
	public ResultModel tradeGoodsUpShelves(List<String> specsTpIdList, Integer centerId, int display) {
		// 判断specsTpIdList是不是都是一般贸易
		List<Integer> types = goodsMapper.getGoodsTypeBySpecsTpIds(specsTpIdList);
		if (types.size() > 1 || types.get(0) != TradePatternEnum.GENERAL_TRADE.getType()) {
			return new ResultModel(false, "请选择一般贸易商品上架");
		}
		// 状态更新为上架
		goodsMapper.updateTradeSpecsUpshelf(specsTpIdList, display);
		// 只有前端展示的时候才需要建lucene索引
		if (TpGoodsDisplayEnum.FRONT.ordinal() == display || TpGoodsDisplayEnum.BOTH.ordinal() == display) {
			// 新上架创建索引
			renderLuceneModel(centerId, specsTpIdList);
		}
		threadPoolComponent.publish(specsTpIdList, centerId);// 发布商品
		// FIXME
		// threadPoolComponent.sendGoodsInfo(specsTpIdList);// 通知对接用户商品上架
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel signalKjGoodsUpShelves(AutoSelectionBO bo, Integer centerId, int display) {
		List<String> specsTpIds = new ArrayList<>();
		specsTpIds.add(bo.getSpecsTpId());
		// 状态更新为上架并绑定itemId
		goodsMapper.updateSignalKjGoodsUpShelves(bo, display);
		// 只有前端展示的时候才需要建lucene索引
		if (TpGoodsDisplayEnum.FRONT.ordinal() == display || TpGoodsDisplayEnum.BOTH.ordinal() == display) {
			// 新上架创建索引
			renderLuceneModel(centerId, specsTpIds);
		}
		threadPoolComponent.publish(specsTpIds, centerId);// 发布商品
		// FIXME
		// threadPoolComponent.sendGoodsInfo(specsTpIdList);// 通知对接用户商品上架
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel batchKjGoodsUpShelves(List<String> specsTpIdList, Integer centerId, int display) {
		// 判断specsTpIdList是不是都是一般贸易
		List<Integer> types = goodsMapper.getGoodsTypeBySpecsTpIds(specsTpIdList);
		if (types.size() > 1 || types.get(0) != TradePatternEnum.CROSS_BORDER.getType()) {
			return new ResultModel(false, "请选择跨境商品上架");
		}
		// 自动选择对应的itemId
		List<AutoSelectionBO> boList = goodsServiceComponent.AutoSelectItemId(specsTpIdList,
				AutoSelectionModeEnum.INTERNAL_PRICE_LOWEST);
		// 状态更新为上架并绑定itemId
		goodsMapper.updateBatchKjGoodsUpShelves(boList, display);
		// 只有前端展示的时候才需要建lucene索引
		if (TpGoodsDisplayEnum.FRONT.ordinal() == display || TpGoodsDisplayEnum.BOTH.ordinal() == display) {
			// 新上架创建索引
			renderLuceneModel(centerId, specsTpIdList);
		}
		threadPoolComponent.publish(specsTpIdList, centerId);// 发布商品
		// FIXME
		// threadPoolComponent.sendGoodsInfo(specsTpIdList);// 通知对接用户商品上架
		return new ResultModel(true, "");
	}

	@Override
	public ResultModel downShelves(List<String> specsTpIdList, Integer centerId) {
		if (specsTpIdList == null || specsTpIdList.size() == 0) {
			return new ResultModel(false, "请传入specsTpId");
		}
		goodsMapper.updateSpecsTpDownShelves(specsTpIdList);// 商品更新为下架状态,同时不可分销

		// lucene下架商品
		deleteLuceneAndDownShelves(specsTpIdList, centerId);
		threadPoolComponent.delPublish(specsTpIdList, centerId);// 删除商品和重新发布商品
		// FIXME
		// threadPoolComponent.sendGoodsInfoDownShelves(specsTpIdList);//
		// 通知对接用户商品下架
		return new ResultModel(true, "");
	}

	private void deleteLuceneAndDownShelves(List<String> specsTpIdList, Integer id) {
		AbstractLucene lucene = LuceneFactory.get(id);
		lucene.deleteIndex(specsTpIdList);
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
	public List<Goods> listGoodsByGoodsIds(List<String> goodsIdList) {

		return goodsMapper.listGoodsByGoodsIds(goodsIdList);
	}
}
