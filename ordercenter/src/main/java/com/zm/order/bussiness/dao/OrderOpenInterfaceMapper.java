package com.zm.order.bussiness.dao;

import com.zm.order.pojo.OrderStatus;

public interface OrderOpenInterfaceMapper {

	OrderStatus getOrderStatus(String orderId);

	void updateOrderPayCustom(String orderId);
}
