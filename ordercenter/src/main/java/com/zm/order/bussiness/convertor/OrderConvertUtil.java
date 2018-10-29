package com.zm.order.bussiness.convertor;

import java.util.ArrayList;
import java.util.List;

import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;

public class OrderConvertUtil {

	public static List<OrderBussinessModel> convertToOrderBussinessModel(OrderInfo orderInfo,StringBuilder sb) {
		List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
		OrderBussinessModel model = null;
		for (OrderGoods goods : orderInfo.getOrderGoodsList()) {
			model = new OrderBussinessModel();
			model.setOrderId(orderInfo.getOrderId());
			model.setItemCode(goods.getItemCode());
			model.setItemId(goods.getItemId());
			model.setQuantity(goods.getItemQuantity());
			model.setSku(goods.getSku());
			model.setItemPrice(goods.getItemPrice());
			list.add(model);
			if(sb != null){
				sb.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
			}
		}
		return list;
	}
}
