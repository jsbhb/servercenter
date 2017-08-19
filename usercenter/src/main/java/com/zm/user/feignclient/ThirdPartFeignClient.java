package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("3rdcenter")
public interface ThirdPartFeignClient {

	@RequestMapping(value="{version}/verify", method=RequestMethod.GET)
	boolean verifyPhoneCode(@PathVariable("version") Double version, @RequestParam("phone") String phone, @RequestParam("code") String code);
}
