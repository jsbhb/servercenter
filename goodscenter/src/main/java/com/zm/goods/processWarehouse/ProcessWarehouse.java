package com.zm.goods.processWarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.goods.bussiness.dao.GoodsMapper;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.processWarehouse.model.WarehouseModel;

@Component
public class ProcessWarehouse {

	@Resource
	GoodsMapper goodsMapper;

	public synchronized ResultModel processWarehouse(Integer orderFlag, List<OrderBussinessModel> orderList) {
		List<String> itemIds = new ArrayList<String>();
		for (OrderBussinessModel model : orderList) {
			itemIds.add(model.getItemId());
		}
		List<WarehouseModel> stockList = goodsMapper.listWarehouse(itemIds);
		return process(stockList, orderList, orderFlag);
	}

	private ResultModel process(List<WarehouseModel> stockList, List<OrderBussinessModel> orderList,
			Integer orderFlag) {
		if (stockList == null || stockList.size() == 0) {
			return new ResultModel(false, "没有对应的商品库存");
		}
		Map<String, WarehouseModel> warehouseMap = new HashMap<String, WarehouseModel>();
		for (WarehouseModel model : stockList) {
			warehouseMap.put(model.getItemId(), model);
		}

		StringBuilder sb = new StringBuilder("商品编号：");
		boolean enough = true;
		for (OrderBussinessModel model : orderList) {
			WarehouseModel warehouse = warehouseMap.get(model.getItemId());
			if (warehouse.getFxqty() < model.getQuantity()) {
				sb.append(model.getItemId() + ",");
				enough = false;
			}
			warehouse.setFrozenqty(model.getQuantity());
		}
		if (!enough) {
			String errorMsg = sb.substring(0, sb.length() - 1) + "库存不足";
			return new ResultModel(false, errorMsg);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("list", stockList);
		param.put("orderFlag", orderFlag);
		goodsMapper.updateStock(param);

		return new ResultModel(true, null);
	}
}
