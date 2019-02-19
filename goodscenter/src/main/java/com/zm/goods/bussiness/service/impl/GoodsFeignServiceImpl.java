package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.component.GoodsServiceComponent;
import com.zm.goods.bussiness.component.PriceComponent;
import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.dao.GoodsTagMapper;
import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.exception.OriginalPriceUnEqual;
import com.zm.goods.exception.WrongPlatformSource;
import com.zm.goods.feignclient.SupplierFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.GoodsItemBO;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.processWarehouse.ProcessWarehouse;
import com.zm.goods.utils.CalculationUtils;

@Service
public class GoodsFeignServiceImpl implements GoodsFeignService {

	@Resource
	GoodsItemMapper goodsItemMapper;
	
	@Resource
	GoodsMapper goodsMapper;
	
	@Resource
	PriceComponent priceComponent;

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
			if(model.getItemId() != null && !"".equals(model.getItemId())){
				itemIds.add(model.getItemId());
			} else {
				entity = new GoodsItemEntity();
				entity.setConversion(model.getConversion());
				entity.setSku(model.getSku());
				itemList.add(entity);
			}
		}
		if(itemIds != null && itemIds.size() > 0){
			receiveList.addAll(goodsItemMapper.listGoodsItemByItemIds(itemIds));
		}
		if(itemList != null && itemList.size() > 0){
			receiveList.addAll(goodsItemMapper.listGoodsItemByParam(itemList));
		}
		if (receiveList == null || receiveList.size() == 0) {
			return new ResultModel(false, "导入的订单商品在系统中没有，请先在系统完善商品");
		} else {
			for(GoodsItemEntity tem : receiveList){
				tem.infoFilter();
			}
		}
		return new ResultModel(true, receiveList);
	}
	
	
	private final String FX = "fx";
	private final String NOT_FX = "notfx";
	private final int DEFAULT_PLATFORMSOURCE = 1;
	@Override
	public ResultModel getPriceAndDelStock(List<OrderBussinessModel> list, Integer supplierId, boolean vip,
			Integer centerId, Integer orderFlag, String couponIds, Integer userId, boolean isFx, int platformSource,
			int gradeId) {

		// 初始化参数
		ResultModel result = new ResultModel(true, "");
		Map<String, Object> map = new HashMap<String, Object>();// 返回结果的map
		Map<Tax, Double> taxMap = new HashMap<Tax, Double>();// 税费map
		Map<String, Object> param = new HashMap<String, Object>();// 参数map
		Map<String, GoodsSpecs> tempSpecsMap = new HashMap<String, GoodsSpecs>();// 规格temp
		Map<String, Tax> tempTaxMap = new HashMap<String, Tax>();// 税费temp
		GoodsSpecs specs = null;
		Double totalAmount = 0.0;
		Double originalPrice = 0.0;
		Integer weight = 0;
		Map<String, GoodsSpecs> specsMap = new HashMap<String, GoodsSpecs>();
		List<String> itemIds = new ArrayList<String>();
		for (OrderBussinessModel model : list) {
			itemIds.add(model.getItemId());
		}
		// 判断所有商品是否都是同个仓库
		param.put("supplierId", supplierId);
		param.put("list", itemIds);
		int count = goodsMapper.countGoodsBySupplierIdAndItemId(param);
		if (count != list.size()) {
			return new ResultModel(false, ErrorCodeEnum.SUPPLIER_GOODS_ERROR.getErrorCode(),
					ErrorCodeEnum.SUPPLIER_GOODS_ERROR.getErrorMsg());
		}
		// 判断订单属性和商品属性是否一致
		try {
			int type = goodsMapper.getOrderGoodsType(param);
			if (!orderFlag.equals(type)) {
				return new ResultModel(false, ErrorCodeEnum.TYPE_ERROR.getErrorCode(),
						ErrorCodeEnum.TYPE_ERROR.getErrorMsg());
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("判断订单属性和商品属性出错", e);
			return new ResultModel(false, ErrorCodeEnum.TYPE_ERROR.getErrorCode(),
					ErrorCodeEnum.TYPE_ERROR.getErrorMsg());
		}

		// 获取所有item的规格
		param.put("list", itemIds);
		param.put("isFx", isFx ? FX : NOT_FX);
		List<GoodsSpecs> specsList = goodsMapper.getGoodsSpecsForOrder(param);
		if (specsList == null || specsList.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
					"所有商品" + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
		}
		for (GoodsSpecs tempspecs : specsList) {
			tempSpecsMap.put(tempspecs.getItemId(), tempspecs);
		}
		// 获取所有税率信息
		List<Tax> taxList = goodsMapper.getTax(itemIds);
		for (Tax tax : taxList) {
			tempTaxMap.put(tax.getItemId(), tax);
		}

		for (OrderBussinessModel model : list) {
			specs = tempSpecsMap.get(model.getItemId());
			if (specs == null) {
				return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
						"itemId=" + model.getItemId() + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
			}
			weight += specs.getWeight() * model.getQuantity();
			Double amount = 0.0;
			try {
				// 这里获取的是商品原总价用来计算税率 platformSource不能传福利商城的 值，vip传false
				amount = goodsServiceComponent.judgeQuantityRange(false, result, specs, model, DEFAULT_PLATFORMSOURCE,
						gradeId);
				originalPrice = CalculationUtils.add(originalPrice, amount);
				LogUtil.writeLog("originalPrice===" + originalPrice);
			} catch (WrongPlatformSource e) {
				return new ResultModel(false, e.getMessage());
			} catch (OriginalPriceUnEqual e1) {
				return new ResultModel(false, e1.getMessage());
			}
			if (!result.isSuccess()) {
				return new ResultModel(false, ErrorCodeEnum.OUT_OF_RANGE.getErrorCode(),
						ErrorCodeEnum.OUT_OF_RANGE.getErrorMsg());
			}
			if (Constants.O2O_ORDER.equals(orderFlag)) {
				Tax tax = tempTaxMap.get(model.getItemId());
				if (taxMap.get(tax) == null) {
					taxMap.put(tax, amount);
				} else {
					taxMap.put(tax, taxMap.get(tax) + amount);
				}
			}

			specsMap.put(model.getItemId(), specs);
		}

		try {
			// 获取商品优惠后的价格
			totalAmount = priceComponent.calPrice(list, specsMap, couponIds, vip, centerId, result, userId,
					platformSource, gradeId);
		} catch (WrongPlatformSource e) {// 福利平台出错
			return new ResultModel(false, e.getMessage());
		} catch (OriginalPriceUnEqual e1) {// 订单商品原价和现在原价不相等，会引起返佣不一样
			return new ResultModel(false, e1.getMessage());
		}
		if (!result.isSuccess()) {
			return result;
		}
		map.put("tax", taxMap);
		map.put("originalPrice", originalPrice);
		map.put("weight", weight);
		map.put("totalAmount", totalAmount);
		result.setSuccess(true);
		result.setObj(map);
		return result;
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
	public ResultModel calStock(List<OrderBussinessModel> list, Integer supplierId, Integer orderFlag) {

		supplierFeignClient.checkStock(Constants.FIRST_VERSION, supplierId, list);

		return processWarehouse.processWarehouse(orderFlag, list);
	}
	
	@Override
	public List<String> listPreSellItemIds() {
		
		return goodsTagMapper.listCutOrderItemIds();
	}
}
