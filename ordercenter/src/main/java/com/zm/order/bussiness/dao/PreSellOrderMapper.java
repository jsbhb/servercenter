package com.zm.order.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;

public interface PreSellOrderMapper {

	Page<OrderInfo> selectForPage(OrderInfo entity);
	
	List<OrderGoods> selectOrderGoodsByOrderIds(List<String> list);

	void passPreSellOrder(List<String> list);
}
