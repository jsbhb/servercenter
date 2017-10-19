package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.user.pojo.ResultModel;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	@RequestMapping(value = "{version}/order/table/{centerId}", method = RequestMethod.POST)
	public ResultModel createTable(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId);
}
