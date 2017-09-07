package com.zm.order.feignclient;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.feignclient.model.PayModel;


@FeignClient("paycenter")
public interface PayFeignClient {

	@RequestMapping(value = "wxpay/{type}/{clientId}/{openId}", method = RequestMethod.POST)
	public Map<String, String> wxPay(@PathVariable("openId") String openId, @PathVariable("clientId") Integer clientId,
			@PathVariable("type") String type, @RequestBody PayModel model) throws Exception;
}