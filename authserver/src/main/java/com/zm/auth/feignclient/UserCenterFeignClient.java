package com.zm.auth.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("usercenter")
public interface UserCenterFeignClient {
	@RequestMapping(value = "{version}/user/phone/{userId}", method = RequestMethod.GET)
	public String getPhoneByUserId(@PathVariable("version") Double version,@PathVariable("userId") Integer userId);
}
