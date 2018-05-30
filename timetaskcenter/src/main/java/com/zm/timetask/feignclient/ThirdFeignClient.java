package com.zm.timetask.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("3rdcenter")
public interface ThirdFeignClient {

	@RequestMapping(value = "{version}/pv/persist", method = RequestMethod.POST)
	public void persistTask(@PathVariable("version") Double version);
}
