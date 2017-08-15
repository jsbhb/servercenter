package com.zm.order.bussiness.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderService;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ResultPojo;
import com.zm.order.utils.CommonUtils;

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
public class OrderServiceImpl implements OrderService {

	@Resource
	OrderMapper orderMapper;

	@Override
	public ResultPojo saveOrder(OrderInfo info) throws DataIntegrityViolationException, Exception{
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
		
		String orderId = CommonUtils.getOrderId(info.getOrderDetail().getOrderFlag() + "");
		info.setOrderId(orderId);
		info.getOrderDetail().setOrderId(orderId);
		
		orderMapper.saveOrder(info);

		orderMapper.saveOrderDetail(info.getOrderDetail());

		for (OrderGoods goods : info.getOrderGoodsList()) {
			goods.setOrderId(orderId);
		}
		orderMapper.saveOrderGoods(info.getOrderGoodsList());

		result.setSuccess(true);
		return result;

	}

	@Override
	public ResultPojo listUserOrder(Map<String, Integer> param) {
		ResultPojo result = new ResultPojo();
		
		result.setObj(orderMapper.listOrderByUser(param));
		result.setSuccess(true);
		
		return result;
	}

	@Override
	public ResultPojo removeUserOrder(Map<String, Object> param) {
		ResultPojo result = new ResultPojo();
		
		orderMapper.removeUserOrder(param);
		
		result.setSuccess(true);
		return result;
	}
	
	@Override
	public ResultPojo confirmUserOrder(Map<String, Object> param) {
		ResultPojo result = new ResultPojo();
		
		orderMapper.confirmUserOrder(param);
		
		result.setSuccess(true);
		return result;
	}
}
