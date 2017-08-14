package com.zm.order.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.pojo.OrderInfo;

/**  
 * ClassName: OrderServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 3:54:27 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */

@Service("orderService")
public class OrderServiceImpl implements OrderService{

	@Resource
	OrderMapper orderMapper;

	@Override
	public void saveOrder(OrderInfo info) {
		
		orderMapper.saveOrder(info);
	}
}
