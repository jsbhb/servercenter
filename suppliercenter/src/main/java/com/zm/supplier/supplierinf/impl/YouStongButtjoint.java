package com.zm.supplier.supplierinf.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderCancelResult;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.util.ButtJointMessageUtils;

@Component
public class YouStongButtjoint extends AbstractSupplierButtJoint {

	private Map<String,Long> timeMap = new HashMap<String, Long>();
	
//	private final String 
	
	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info) {
		Map<String, String> param = ButtJointMessageUtils.getYouStongOrder(info);
		return null;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<OrderCancelResult> orderCancel(OrderInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

}
