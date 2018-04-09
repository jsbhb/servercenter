package com.zm.order.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.log.model.ExceptionLog;
import com.zm.order.log.model.LogInfo;
import com.zm.order.log.model.OpenInfLog;
import com.zm.order.pojo.ResultModel;

@FeignClient("logcenter")
public interface LogFeignClient {

	@RequestMapping(value="{version}/exception/log", method = RequestMethod.POST)
	ResultModel saveExceptionLog(@PathVariable("version") Double version, ExceptionLog logInfo);

	@RequestMapping(value="{version}/openInfoLog/log", method = RequestMethod.POST)
	ResultModel saveOpenInfoLog(@PathVariable("version") Double version, OpenInfLog log);

	@RequestMapping(value="{version}/logInfo/log", method = RequestMethod.POST)
	ResultModel saveLogInfo(@PathVariable("version") Double version, LogInfo logInfo);
	
}
