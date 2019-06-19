package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.OrderGoodsDTO;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.CustomCompletion;
import com.zm.goods.pojo.bo.GoodsItemBO;
import com.zm.goods.utils.JSONUtil;

@Service
public class GoodsFeignServiceImpl implements GoodsFeignService {

	@Resource
	GoodsItemMapper goodsItemMapper;
	
	@Resource
	GoodsMapper goodsMapper;

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

	@Override
	public List<CustomCompletion> customCompletion(List<String> itemIdList) {
		
		return goodsMapper.listGoodsInfoForCstomCompletion(itemIdList);
	}

	@Override
	public String getGoodsItemProxyPrice(List<String> itemIdList) {
		
		List<GoodsPrice> priceList = goodsMapper.listGoodsItemProxyPrice(itemIdList);
		Map<String,Double> tmpMap = new HashMap<>();
		for(GoodsPrice price : priceList){
			tmpMap.put(price.getItemId(), price.getProxyPrice());
		}
		return JSONUtil.toJson(tmpMap);
	}

	@Override
	public void updateGoodsSale(List<OrderGoodsDTO> orderGoodsList) {
		goodsMapper.updateGoodsSale(orderGoodsList);
	}

}
