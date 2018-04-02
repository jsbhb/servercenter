package com.zm.order.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.order.pojo.OrderInfo;

public interface PreSellOrderService {

	/**
	 * queryByPage:分页查询订单信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<OrderInfo> queryByPage(OrderInfo entity);

	boolean passPreSellOrder(List<String> orderIdList);
}
