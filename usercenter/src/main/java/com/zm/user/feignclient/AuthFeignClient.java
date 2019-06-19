package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.user.common.ResultModel;
import com.zm.user.feignclient.model.UserInfo;

@FeignClient("authcenter")
public interface AuthFeignClient {

	@RequestMapping(value = "auth/login", method = RequestMethod.POST)
	public ResultModel createAuthenticationToken(@RequestBody UserInfo userInfo) throws Exception;
}
