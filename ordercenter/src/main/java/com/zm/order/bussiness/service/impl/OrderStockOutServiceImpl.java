/**  
 * Project Name:ordercenter  
 * File Name:OrderBackServiceImpl.java  
 * Package Name:com.zm.order.bussiness.service.impl  
 * Date:Jan 1, 20182:49:06 PM  
 *  
 */
package com.zm.order.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.order.bussiness.dao.OrderMapper;
import com.zm.order.bussiness.dao.OrderStockOutMapper;
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.UserFeignClient;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.OrderInfoListForDownload;
import com.zm.order.pojo.ThirdOrderInfo;

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
	
	@Resource
	UserFeignClient userFeignClient;
	
	@Override
	public Page<OrderInfo> queryByPage(OrderInfo entity) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("entity", entity);
		entity.init();
		if(entity.getShopId() != null){
			List<Integer> childrenIds = userFeignClient.listChildrenGrade(Constants.FIRST_VERSION, entity.getShopId());
			param.put("list", childrenIds);
		}
		Integer count = orderBackMapper.queryCountOrderInfo(param);
		List<OrderInfo> orderList = orderBackMapper.selectForPage(param);
		entity.setTotalRows(count);
		entity.webListConverter();//计算总页数
		Page<OrderInfo> page = new Page<OrderInfo>(entity.getCurrentPage(), entity.getNumPerPage(), count);
		page.addAll(orderList);
		page.setPages(entity.getTotalPages());
		return page;
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

	@Override
	public List<ThirdOrderInfo> queryThirdInfo(String orderId) {
		return orderMapper.getThirdInfo(orderId);
	}

	@Override
	public List<OrderInfoListForDownload> queryOrdreListForDownload(String startTime, String endTime, String gradeId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		List<Integer> childrenIds = userFeignClient.listChildrenGrade(Constants.FIRST_VERSION, Integer.parseInt(gradeId));
		param.put("list", childrenIds);
		return orderBackMapper.selectOrdreListForDownload(param);
	}

}
