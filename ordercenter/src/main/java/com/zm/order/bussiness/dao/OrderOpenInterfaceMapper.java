package com.zm.order.bussiness.dao;

import java.util.Map;

import com.zm.order.pojo.OrderStatus;

public interface OrderOpenInterfaceMapper {

	OrderStatus getOrderStatus(String orderId);

	void updateOrderStatus(Map<String, Object> param);
}
