package com.zm.order.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.ConfigConstants;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ResultPojo;

/**
 * ClassName: OrderController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 11, 2017 11:27:08 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class OrderController {

	@Resource
	OrderService orderService;
	
	
	@RequestMapping(value = "{version}/order/creative", method = RequestMethod.GET)
	public ResultPojo createOrder(@PathVariable("version") Double version, OrderInfo orderInfo) {
		
		ResultPojo result = new ResultPojo();
		//设置允许跨域请求
//		res.setHeader(ConfigConstants.CROSS_DOMAIN, ConfigConstants.DOMAIN_NAME);
		
		if (ConfigConstants.FIRST_VERSION.equals(version)) {
			orderService.saveOrder(orderInfo);
		}
		return result;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
		return 2;
	}
}