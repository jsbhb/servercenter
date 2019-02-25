package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.component.GoodsServiceComponent;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.AutoSelectionModeEnum;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.enummodel.TradePatternEnum;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.feignclient.SupplierFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.AutoSelectionBO;
import com.zm.goods.pojo.bo.DealOrderDataBO;
import com.zm.goods.pojo.bo.GoodsItemBO;
import com.zm.goods.pojo.bo.OrderGoodsCompleteBO;
import com.zm.goods.pojo.po.GoodsPricePO;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.processWarehouse.ProcessWarehouse;

@Service
public class GoodsFeignServiceImpl implements GoodsFeignService {

	@Resource
	GoodsItemMapper goodsItemMapper;

	@Resource
	GoodsMapper goodsMapper;

	@Resource
	GoodsServiceComponent goodsServiceComponent;

	@Resource
	ProcessWarehouse processWarehouse;

	@Resource
	SupplierFeignClient supplierFeignClient;

	@Resource
	GoodsTagMapper goodsTagMapper;

	@Override
	public ResultModel manualOrderGoodsCheck(List<GoodsItemBO> list) {
		if (list == null || list.size() == 0) {
			return new ResultModel(false, "没有商品信息");
		}
		List<String> itemIds = new ArrayList<String>();
		List<GoodsItemEntity> itemList = new ArrayList<GoodsItemEntity>();
		List<GoodsItemEntity> receiveList = new ArrayList<GoodsItemEntity>();
		GoodsItemEntity entity = null;
		for (GoodsItemBO model : list) {
			if (model.getItemId() != null && !"".equals(model.getItemId())) {
				itemIds.add(model.getItemId());
			} else {
				entity = new GoodsItemEntity();
				entity.setConversion(model.getConversion());
				entity.setSku(model.getSku());
				itemList.add(entity);
			}
		}
		if (itemIds != null && itemIds.size() > 0) {
			receiveList.addAll(goodsItemMapper.listGoodsItemByItemIds(itemIds));
		}
		if (itemList != null && itemList.size() > 0) {
			receiveList.addAll(goodsItemMapper.listGoodsItemByParam(itemList));
		}
		if (receiveList == null || receiveList.size() == 0) {
			return new ResultModel(false, "导入的订单商品在系统中没有，请先在系统完善商品");
		} else {
			for (GoodsItemEntity tem : receiveList) {
				tem.infoFilter();
			}
		}
		return new ResultModel(true, receiveList);
	}

	private final String FX = "fx";
	private final String NOT_FX = "notfx";

	@Override
	public ResultModel getPriceAndDelStock(DealOrderDataBO bo) {
		// 获取贸易类型：跨境/一般贸易
		TradePatternEnum tpe = TradePatternEnum.valueof(bo.getOrderFlag());
		switch (tpe) {
		case CROSS_BORDER:
			return dealOrderGoodsByCrossBorder(bo);
		case GENERAL_TRADE:
			return dealOrderGoodsByGeneralTrade(bo);
		}
		return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
				ErrorCodeEnum.MISSING_PARAM.getErrorMsg() + "orderFlag");
	}

	/**
	 * @fun 处理一般贸易订单商品
	 * @param bo
	 * @return
	 */
	private ResultModel dealOrderGoodsByGeneralTrade(DealOrderDataBO bo) {
		// 判断状态和价格
		List<String> specsTpIds = bo.getModelList().stream().map(m -> m.getSpecsTpId()).collect(Collectors.toList());
		Map<String, Object> param = new HashMap<>();
		param.put("isFx", bo.isFx() ? FX : NOT_FX);
		param.put("specsTpIds", specsTpIds);
		List<GoodsSpecsTradePattern> specsTpList = goodsMapper.getGoodsSpecsTpByParam(param);
		ResultModel result = judgeStatusAndPrice(bo, specsTpList);
		if (!result.isSuccess()) {// 判断失败直接返回
			return result;
		}
		// 自动分配itemId
		List<AutoSelectionBO> selectionList = goodsServiceComponent.AutoSelectForOrder(bo,
				AutoSelectionModeEnum.DEFAULT);
		if (selectionList.size() == 0) {// 人工分配直接返回
			return new ResultModel(true, new ArrayList<OrderGoodsCompleteBO>());
		}
		Map<String, String> map = selectionList.stream()
				.collect(Collectors.toMap(AutoSelectionBO::getSpecsTpId, tmp -> tmp.getItemId()));
		bo.getModelList().stream().forEach(m -> {
			m.setItemId(map.get(m.getSpecsTpId()));
		});
		// 判断购买数量是否在指定范围
		result = judgeBuyRange(bo);
		if (!result.isSuccess()) {// 判断失败直接返回
			return result;
		}
		// 判断库存
		result = processWarehouse.processWarehouse(bo.getModelList());
		if (!result.isSuccess()) {// 判断失败直接返回
			return result;
		}
		List<OrderGoodsCompleteBO> boList = createOgcList(specsTpList);
		result.setObj(boList);
		return result;
	}

	/**
	 * @fun 处理跨境订单的商品
	 * @param bo
	 * @return
	 */
	private ResultModel dealOrderGoodsByCrossBorder(DealOrderDataBO bo) {
		List<String> itemIdList = bo.getModelList().stream().map(m -> m.getItemId()).collect(Collectors.toList());
		Map<String, Object> param = new HashMap<>();
		// 跨境订单判断商品是否是同一个仓库
		param.put("supplierId", bo.getSupplierId());
		param.put("list", itemIdList);
		int count = goodsMapper.countGoodsBySupplierIdAndItemId(param);
		if (count != itemIdList.size()) {
			return new ResultModel(false, ErrorCodeEnum.SUPPLIER_GOODS_ERROR.getErrorCode(),
					ErrorCodeEnum.SUPPLIER_GOODS_ERROR.getErrorMsg());
		}
		// 判断订单属性和商品属性是否一致
		List<Integer> types = goodsMapper.getOrderGoodsType(param);
		if (types.size() > 1 || !bo.getOrderFlag().equals(types.get(0))) {
			return new ResultModel(false, ErrorCodeEnum.TYPE_ERROR.getErrorCode(),
					ErrorCodeEnum.TYPE_ERROR.getErrorMsg());
		}
		// 判断状态和价格
		List<String> specsTpIds = bo.getModelList().stream().map(m -> m.getSpecsTpId()).collect(Collectors.toList());
		param.put("isFx", bo.isFx() ? FX : NOT_FX);
		param.put("specsTpIds", specsTpIds);
		List<GoodsSpecsTradePattern> specsTpList = goodsMapper.getGoodsSpecsTpByParam(param);
		ResultModel result = judgeStatusAndPrice(bo, specsTpList);
		if (!result.isSuccess()) {// 判断失败直接返回
			return result;
		}
		// 判断购买数量是否在指定范围
		result = judgeBuyRange(bo);
		if (!result.isSuccess()) {// 判断失败直接返回
			return result;
		}
		// 判断库存
		result = processWarehouse.processWarehouse(bo.getModelList());
		if (!result.isSuccess()) {// 判断失败直接返回
			return result;
		}
		List<OrderGoodsCompleteBO> boList = createOgcList(specsTpList);
		result.setObj(boList);
		return result;
	}

	/**
	 * @fun 判断购买数量
	 * @param bo
	 * @param itemIdList
	 * @return
	 */
	private ResultModel judgeBuyRange(DealOrderDataBO bo) {
		List<String> itemIdList = bo.getModelList().stream().map(m -> m.getItemId()).collect(Collectors.toList());
		List<GoodsPricePO> priceList = goodsMapper.listGoodsPriceByItemIds(itemIdList);
		Map<String, List<GoodsPricePO>> map = priceList.stream()
				.collect(Collectors.groupingBy(price -> price.getItemId()));
		for (OrderBussinessModel model : bo.getModelList()) {
			int min = map.get(model.getItemId()).stream().sorted(Comparator.comparing(GoodsPricePO::getMin)).findFirst()
					.get().getMin();
			if (min > model.getQuantity()) {
				return new ResultModel(false, ErrorCodeEnum.OUT_OF_RANGE.getErrorCode(),
						"itemId=" + model.getItemId() + ErrorCodeEnum.OUT_OF_RANGE.getErrorMsg());
			}
		}
		return new ResultModel(true, null);
	}

	/**
	 * @fun 判断价格和状态
	 * @param bo
	 * @return
	 */
	private ResultModel judgeStatusAndPrice(DealOrderDataBO bo, List<GoodsSpecsTradePattern> specsTpList) {
		try {
			calRetailPriceByPlatform(bo, specsTpList);
		} catch (WrongPlatformSource e) {
			LogUtil.writeErrorLog("平台价格计算", e);
			return new ResultModel(false, e.getMessage());
		}
		if (specsTpList == null || specsTpList.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
					"所有商品" + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
		}
		// 判断是否有部分商品下架或不可分销/价格判断
		Map<String, GoodsSpecsTradePattern> tmpMap = specsTpList.stream()
				.collect(Collectors.toMap(GoodsSpecsTradePattern::getSpecsTpId, tp -> tp));
		for (OrderBussinessModel model : bo.getModelList()) {
			GoodsSpecsTradePattern specs = tmpMap.get(model.getItemId());
			if (specs == null) {
				return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
						"specsTpId=" + model.getSpecsTpId() + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
			}
			if (model.getItemPrice() != specs.getRetailPrice()) {
				return new ResultModel(false, ErrorCodeEnum.RETAIL_PRICE_ERROR.getErrorCode(),
						"specsTpId=" + model.getSpecsTpId() + ErrorCodeEnum.RETAIL_PRICE_ERROR.getErrorMsg());
			}
		}
		return new ResultModel(true, null);
	}

	/**
	 * @fun 根据不同平台设置零售价零售价
	 * @throws WrongPlatformSource
	 */
	private void calRetailPriceByPlatform(DealOrderDataBO bo, List<GoodsSpecsTradePattern> specsTpList)
			throws WrongPlatformSource {
		// 设置价格
		switch (bo.getPlatformSource()) {
		case Constants.WELFARE_WEBSITE:
			for (GoodsSpecsTradePattern specs : specsTpList) {
				goodsServiceComponent.getWelfareWebsitePriceInterval(specs, specs.getDiscount(), bo.getGradeId());
			}
			break;
		case Constants.BACK_MANAGER_WEBSITE:
			for (GoodsSpecsTradePattern specs : specsTpList) {
				goodsServiceComponent.getBackWebsitePriceInterval(specs, specs.getDiscount(), bo.getGradeId());
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @fun 创建订单商品补全返回对象
	 * @param bo
	 * @return
	 * @throws WrongPlatformSource
	 */
	private List<OrderGoodsCompleteBO> createOgcList(List<GoodsSpecsTradePattern> specsTpList) {
		List<String> specsIdList = specsTpList.stream().map(tp -> tp.getSpecsId()).collect(Collectors.toList());
		List<String> itemIds = specsTpList.stream().map(tp -> tp.getItemId()).collect(Collectors.toList());
		List<GoodsSpecs> specsList = goodsMapper.listGoodsSpecsBySpecsIds(specsIdList);
		List<Items> itemsList = goodsMapper.listItemsByItemIds(itemIds);
		Map<String, GoodsSpecs> map = specsList.stream().collect(Collectors.toMap(GoodsSpecs::getSpecsId, s -> s));
		Map<String, Items> map1 = itemsList.stream().collect(Collectors.toMap(Items::getItemId, i -> i));
		OrderGoodsCompleteBO bo = null;
		GoodsSpecs s = null;
		Items i = null;
		List<OrderGoodsCompleteBO> boList = new ArrayList<>();
		for (GoodsSpecsTradePattern specsTp : specsTpList) {
			bo = new OrderGoodsCompleteBO();
			s = map.get(specsTp.getSpecsId());
			i = map1.get(specsTp.getItemId());
			bo.setCarton(s.getCarton());
			bo.setConversion(s.getConversion());
			bo.setExciseTax(specsTp.getExciseTax());
			bo.setIncrementTax(specsTp.getIncrementTax());
			bo.setItemId(specsTp.getItemId());
			bo.setSpecsTpId(specsTp.getSpecsTpId());
			bo.setWeight(s.getWeight());
			bo.setSku(i.getSku());
			bo.setItemCode(i.getItemCode());
			bo.setUnit(i.getUnit());
			bo.setSupplierId(i.getSupplierId());
			bo.setSupplierName(i.getSupplierName());
			bo.setItemPrice(specsTp.getRetailPrice());
			boList.add(bo);
		}
		return boList;
	}

	@Override
	public void stockBack(List<OrderBussinessModel> list, Integer orderFlag) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		param.put("orderFlag", orderFlag);
		goodsMapper.updateStockBack(param);
	}

	@Override
	public ResultModel stockJudge(List<OrderBussinessModel> list, Integer orderFlag, Integer supplierId) {
		supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);
		ResultModel resultModel = processWarehouse.processWarehouse(list);
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
	public List<OrderBussinessModel> checkStock() {
		return goodsMapper.checkStock();
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
	public List<String> listPreSellItemIds() {

		return goodsTagMapper.listCutOrderItemIds();
	}
}
