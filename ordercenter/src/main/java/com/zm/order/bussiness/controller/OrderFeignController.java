package com.zm.order.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderFeignService;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.bo.CustomOrderReturn;
import com.zm.order.pojo.bo.GradeBO;

/**
 * @fun 订单控制器类，供feign调用
 * @author user
 *
 */
@RestController
public class OrderFeignController {

	@Resource
	OrderFeignService orderFeignService;

	/**
	 * @fun 用户中心新增grade需要通知此接口，去同步最新的grade
	 * @param version
	 */
	@RequestMapping(value = "{version}/order/feign/notice", method = RequestMethod.POST)
	public void noticeToAddGrade(@PathVariable("version") Double version, @RequestBody GradeBO grade) {
		if (Constants.FIRST_VERSION.equals(version)) {
			orderFeignService.syncGrade(grade);
		}
	}

	/**
	 * @fun 保存订单对应的物流企业信息，用于申报失败重新申报时有记录可拿
	 * @param version
	 * @param orderExpress
	 */
	@RequestMapping(value = "{version}/custom/order/express", method = RequestMethod.POST)
	public void saveCustomOrderExpress(@PathVariable("version") Double version,
			@RequestBody CustomOrderReturn orderreturn) {

		if (Constants.FIRST_VERSION.equals(version)) {
			orderFeignService.saveCustomOrderExpress(orderreturn);
		}
	}

	/**
	 * @fun 根据订单号获取订单商品信息用于海关实时获取支付信息用
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "custom/realtimedataup/ordergoods/{orderId}", method = RequestMethod.GET)
	public String listOrderGoodsInfo(@PathVariable("orderId") String orderId) {

		return orderFeignService.listOrderGoodsInfo(orderId);
	}
	
	/**
	 * @fun 根据订单号获取交易流水号
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "custom/realtimedataup/detail/{orderId}", method = RequestMethod.GET)
	public String getOrderDetail(@PathVariable("orderId") String orderId) {

		return orderFeignService.getOrderDetail(orderId);
	}
	
	/**
	 * @fun 获取订单对应的详细物流信息
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/order/express/detail/{orderId}", method = RequestMethod.GET)
	public String getOrderExpressDetail(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return orderFeignService.getOrderExpressDetail(orderId);
		}
		return null;
	}
}
