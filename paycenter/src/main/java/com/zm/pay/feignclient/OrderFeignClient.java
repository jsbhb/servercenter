package com.zm.pay.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.pay.pojo.ResultModel;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	@RequestMapping(value = "{version}/order/getClientId/{orderId}", method = RequestMethod.GET)
	Integer getClientIdByOrderId(@PathVariable("orderId") String orderId, @PathVariable("version") Double version);

	@RequestMapping(value = "{version}/order/alread-pay/{orderId}", method = RequestMethod.PUT)
	public ResultModel updateOrderPayStatusByOrderId(@PathVariable("version") Double version,
			@PathVariable("orderId") String orderId);
}
