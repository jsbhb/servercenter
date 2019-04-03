package com.zm.thirdcenter.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("paycenter")
public interface PayClient {

	@RequestMapping(value="/origindata/{orderId}", method = RequestMethod.GET)
	public String listPayOriginData(@PathVariable("orderId") String orderId);
}
