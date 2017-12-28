package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("activitycenter")
public interface ActivityFeignClient {

	@RequestMapping(value = "{version}/createTable/{centerId}", method = RequestMethod.POST)
	public boolean createTable(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId);
}
