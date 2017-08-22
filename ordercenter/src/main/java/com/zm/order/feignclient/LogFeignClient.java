package com.zm.order.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.feignclient.model.LogInfo;
import com.zm.order.pojo.ResultPojo;

@FeignClient("logcenter")
public interface LogFeignClient {

	@RequestMapping(value="1.0/log", method = RequestMethod.POST)
	ResultPojo saveLog(LogInfo logInfo);
	
}
