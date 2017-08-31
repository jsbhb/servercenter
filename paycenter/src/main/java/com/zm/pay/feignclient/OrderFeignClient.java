package com.zm.pay.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	@RequestMapping(value = "{version}/order/getClientId/{orderId}", method = RequestMethod.GET)
	Integer getClientIdByOrderId(@PathVariable("orderId") String orderId,
			@PathVariable("version") Double version);
}
