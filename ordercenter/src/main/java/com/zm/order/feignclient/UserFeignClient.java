package com.zm.order.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "{version}/user/vip/{centerId}/{userId}", method = RequestMethod.GET)
	public boolean getVipUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId);
}
