package com.zm.order.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.common.ResultModel;
import com.zm.order.pojo.OrderInfoEntityForMJY;

@FeignClient("3rdcenter")
public interface ThirdPartFeignClient {

	@RequestMapping(value = "auth/{version}/maijiayun/addStoreSio", method = RequestMethod.POST)
	public ResultModel addStoreSio(@PathVariable("version") Double version, @RequestBody OrderInfoEntityForMJY info);
	
	@RequestMapping(value = "auth/{version}/maijiayun/addStoreSoo", method = RequestMethod.POST)
	public ResultModel addStoreSoo(@PathVariable("version") Double version, @RequestBody OrderInfoEntityForMJY info);
}
