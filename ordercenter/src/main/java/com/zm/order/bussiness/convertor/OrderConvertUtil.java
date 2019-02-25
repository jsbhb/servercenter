package com.zm.order.bussiness.convertor;

import java.util.ArrayList;
import java.util.List;

import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.bo.DealOrderDataBO;

public class OrderConvertUtil {

	public static DealOrderDataBO convertToDealOrderDataBO(OrderInfo orderInfo, StringBuilder sb, boolean vip,
			boolean fx) {
		DealOrderDataBO bo = new DealOrderDataBO();
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
			if (sb != null) {
				sb.append(goods.getItemName() + "*" + goods.getItemQuantity() + ";");
			}
		}
		bo.setCenterId(orderInfo.getCenterId());
		bo.setFx(fx);
		bo.setGradeId(orderInfo.getShopId());
		bo.setCouponIds(orderInfo.getCouponIds());
		bo.setModelList(list);
		bo.setOrderFlag(orderInfo.getOrderFlag());
		bo.setPlatformSource(orderInfo.getOrderSource());
		bo.setUserId(orderInfo.getUserId());
		bo.setVip(vip);
		return bo;
	}
}
