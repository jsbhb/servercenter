package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.user.common.ResultModel;
import com.zm.user.feignclient.model.LogInfo;

@FeignClient("logcenter")
public interface LogFeignClient {

	@RequestMapping(value="{version}/log", method = RequestMethod.POST)
	ResultModel saveLog(@PathVariable("version") Double version,LogInfo logInfo);
}
