package com.zm.order.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ResultPojo;

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
	public ResultPojo saveOrder(OrderInfo info) {
		ResultPojo result = new ResultPojo();
		if (info == null) {
			result.setErrorMsg("订单不能为空");
			result.setSuccess(false);
			return result;
		}
		if (info.getOrderDetail() == null) {
			result.setErrorMsg("订单详情不能为空");
			result.setSuccess(false);
			return result;
		}
		if (info.getOrderGoodsList() == null || info.getOrderGoodsList().size() == 0) {
			result.setErrorMsg("订单商品不能为空");
			result.setSuccess(false);
			return result;
		}
		orderMapper.saveOrder(info);

		orderMapper.saveOrderDetail(info.getOrderDetail());
		
		orderMapper.saveOrderGoods(info.getOrderGoodsList());
		
		result.setSuccess(true);
		return result;

	}
}
