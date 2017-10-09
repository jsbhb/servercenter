package com.zm.timetask.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.timetask.pojo.CustomModel;


@FeignClient("paycenter")
public interface PayFeignClient {

	@RequestMapping(value = "pay/paycustom", method = RequestMethod.POST)
	public boolean payCustom(@RequestBody CustomModel model) throws Exception;
}
