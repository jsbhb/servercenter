package com.zm.goods.processWarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.processWarehouse.model.WarehouseModel;

@Component
public class ProcessWarehouse {

	@Resource
	GoodsMapper goodsMapper;
	
	public synchronized boolean processWarehouse(Integer centerId, Integer orderFlag, List<OrderBussinessModel> orderList){
		Map<String,Object> param = new HashMap<String,Object>();
		List<String> itemIds = new ArrayList<String>();
		for(OrderBussinessModel model : orderList){
			itemIds.add(model.getItemId());
		}
		param.put("list", itemIds);
		if(!Constants.O2O_ORDER.equals(orderFlag)){
			param.put("centerId", "_"+centerId);
		} else {
			param.put("centerId", "");
		}
		List<WarehouseModel> stockList = goodsMapper.listWarehouse(param);
		return process(stockList, orderList, param);
	}
	
	private boolean process(List<WarehouseModel> stockList, List<OrderBussinessModel> orderList, Map<String,Object> param){
		if(stockList == null || stockList.size() == 0){
			return false;
		}
		Map<String, WarehouseModel> warehouseMap = new HashMap<String, WarehouseModel>();
		for(WarehouseModel model : stockList){
			warehouseMap.put(model.getItemId(), model);
		}
		
		for(OrderBussinessModel model : orderList){
			WarehouseModel warehouse = warehouseMap.get(model.getItemId());
			if(warehouse.getSjqty() < model.getQuantity()){
				return false;
			}
			warehouse.setFrozenqty(model.getQuantity());
		}
		param.put("list", stockList);
		goodsMapper.updateStock(param);
		
		return true;
	}
}
