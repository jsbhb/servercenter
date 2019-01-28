package com.zm.thirdcenter.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.thirdcenter.feignclient.model.ThirdLogin;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "auth/{version}/user/3rdLogin-check", method = RequestMethod.POST)
	public boolean get3rdLoginUser(@PathVariable("version") Double version,
			@RequestBody ThirdLogin info);
}
