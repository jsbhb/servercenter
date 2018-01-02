/**  
 * Project Name:ordercenter  
 * File Name:OrderBackServiceImpl.java  
 * Package Name:com.zm.order.bussiness.service.impl  
 * Date:Jan 1, 20182:49:06 PM  
 *  
 */
package com.zm.order.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.dao.OrderStockOutMapper;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;

/**  
 * ClassName: OrderBackServiceImpl <br/>  
 * Function: 后台订单操作服务类. <br/>   
 * date: Jan 1, 2018 2:49:06 PM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7  
 */
@Service
public class OrderStockOutServiceImpl implements OrderStockOutService {

	@Resource
	OrderStockOutMapper orderBackMapper;
	
	@Resource
	OrderMapper orderMapper;
	
	@Override
	public Page<OrderInfo> queryByPage(OrderInfo entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return orderBackMapper.selectForPage(entity);
	}

	@Override
	public OrderInfo queryByOrderId(String orderId) {
		return orderMapper.getOrderByOrderId(orderId);
	}

	@Override
	public Page<OrderGoods> queryByPageForGoods(OrderGoods entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return orderBackMapper.selectOrderGoodsForPage(entity);
	}

}
