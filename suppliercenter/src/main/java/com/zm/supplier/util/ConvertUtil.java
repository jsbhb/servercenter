package com.zm.supplier.util;

import java.util.HashSet;
import java.util.Set;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.model.EdbGoodsInfo;
import com.zm.supplier.supplierinf.model.EdbGoodsInfoList;
import com.zm.supplier.supplierinf.model.EdbStockList;
import com.zm.supplier.supplierinf.model.EdbStockModel;

public class ConvertUtil {

	public static Set<ThirdWarehouseGoods> ConverToThirdWarehouseGoods(EdbGoodsInfoList entity) {
		Set<ThirdWarehouseGoods> result = new HashSet<ThirdWarehouseGoods>();
		ThirdWarehouseGoods goods = null;
		for(EdbGoodsInfo info : entity.getList()){
			goods = new ThirdWarehouseGoods();
			goods.setBrand(info.getBrandName());
			goods.setGoodsName(info.getProName());
			goods.setItemCode(info.getBarCode());
			goods.setRoughWeight(info.getWeight());
			goods.setSku(info.getBarCode());
			result.add(goods);
		}
		return result;
	}

	public static Set<CheckStockModel> ConverToCheckStockModel(EdbStockList edbStockList) {
		Set<CheckStockModel> result = new HashSet<CheckStockModel>();
		CheckStockModel model = null;
		if(edbStockList.getEdbStockList() != null && edbStockList.getEdbStockList().size() > 0){
			for(EdbStockModel edbStockModel :edbStockList.getEdbStockList()){
				model = new CheckStockModel();
				model.setItemCode(edbStockModel.getBarCode());
				model.setQuantity(edbStockModel.getSellStock());
				result.add(model);
			}
		}
		return result;
	}

}
