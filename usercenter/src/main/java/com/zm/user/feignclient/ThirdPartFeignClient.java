package com.zm.user.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.user.common.ResultModel;
import com.zm.user.pojo.NotifyMsg;

@FeignClient("3rdcenter")
public interface ThirdPartFeignClient {

	@RequestMapping(value="auth/{version}/third-part/phoneVerify", method=RequestMethod.GET)
	boolean verifyPhoneCode(@PathVariable("version") Double version, @RequestParam("phone") String phone, @RequestParam("code") String code);
	
	@RequestMapping(value = "{version}/third-part/msg", method = RequestMethod.POST)
	public void notifyMsg(@PathVariable("version") Double version,@RequestBody NotifyMsg notifyMsg);
	
	@RequestMapping(value = "{version}/third-part/phone", method = RequestMethod.POST)
	public ResultModel sendCode(@PathVariable("version") Double version, @RequestBody List<NotifyMsg> notifyList);
}
