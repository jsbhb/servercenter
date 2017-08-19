package com.zm.order.bussiness.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderService;
import com.zm.order.constants.Constants;
import com.zm.order.feignclient.LogFeignClient;
import com.zm.order.feignclient.model.LogInfo;
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
	
	@Resource
	LogFeignClient logFeignClient;

	@RequestMapping(value = "{version}/order/creative", method = RequestMethod.POST)
	public ResultPojo createOrder(@PathVariable("version") Double version, OrderInfo orderInfo,
			HttpServletResponse res) {

		ResultPojo result = new ResultPojo();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {

			try {
				result = orderService.saveOrder(orderInfo);
			} catch (DataIntegrityViolationException e) {
				result.setSuccess(false);
				result.setErrorMsg("请确认字段是否填写完全");
				return result;

			} catch (Exception e) {
				result.setSuccess(false);
				result.setErrorMsg("微服务出现问题");
				return result;
			}
		}
		return result;
	}

	@RequestMapping(value = "{version}/order/{userId}/listOrder", method = RequestMethod.GET)
	public ResultPojo listUserOrder(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			HttpServletRequest req, HttpServletResponse res) {

		ResultPojo result = new ResultPojo();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		Map<String, Integer> param = new HashMap<String, Integer>();
		try {
			String orderFlag = req.getParameter("orderFlag");
			String status = req.getParameter("status");
			if(StringUtils.isEmpty(orderFlag) || userId == null){
				result.setSuccess(false);
				result.setErrorMsg("参数不全");
				return result;
			}

			param.put("orderFlag", Integer.valueOf(orderFlag));
			param.put("userId", userId);
			if (status != null && !"".equals(status)) {
				param.put("status", Integer.valueOf(status));
			}
		} catch (NumberFormatException e) {
			result.setSuccess(false);
			result.setErrorMsg("参数有误");
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.listUserOrder(param);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/{userId}/{orderId}", method = RequestMethod.DELETE)
	public ResultPojo removeUserOrder(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("orderId") String orderId, HttpServletRequest req, HttpServletResponse res) {

		ResultPojo result = new ResultPojo();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("userId", userId);
		param.put("orderId", orderId);

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.removeUserOrder(param);
		}

		return result;
	}

	@RequestMapping(value = "{version}/order/{userId}/{orderId}/confirm", method = RequestMethod.PUT)
	public ResultPojo confirmUserOrder(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("orderId") String orderId, HttpServletRequest req, HttpServletResponse res) {

		ResultPojo result = new ResultPojo();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("userId", userId);
		param.put("orderId", orderId);

		if (Constants.FIRST_VERSION.equals(version)) {
			result = orderService.confirmUserOrder(param);
		}

		return result;
	}
	
	@RequestMapping(value = "/test",method=RequestMethod.GET)
	public ResultPojo test(){
		
		LogInfo logInfo = new LogInfo();
		
		return logFeignClient.saveLog(logInfo);
	}

}