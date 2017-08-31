package com.zm.pay.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.pay.feignclient.model.LogInfo;
import com.zm.pay.pojo.ResultModel;

@FeignClient("logcenter")
public interface LogFeignClient {

	@RequestMapping(value="1.0/log", method = RequestMethod.POST)
	ResultModel saveLog(LogInfo logInfo);
}
