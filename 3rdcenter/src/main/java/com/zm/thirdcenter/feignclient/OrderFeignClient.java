package com.zm.thirdcenter.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ordercenter")
public interface OrderFeignClient {
	
	@RequestMapping(value = "custom/realtimedataup/ordergoods/{orderId}", method = RequestMethod.GET)
	public String listOrderGoodsInfo(@PathVariable("orderId") String orderId);
	
	@RequestMapping(value = "custom/realtimedataup/detail/{orderId}", method = RequestMethod.GET)
	public String getOrderDetail(@PathVariable("orderId") String orderId);
}
