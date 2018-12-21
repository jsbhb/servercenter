package com.zm.goods.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.goods.feignclient.model.AppletCodeParameter;
import com.zm.goods.pojo.ResultModel;

@FeignClient("3rdcenter")
public interface ThirdFeignClient {

	@RequestMapping(value = "{version}/getwxacodeunlimit", method = RequestMethod.POST)
	public ResultModel getAppletCode(@PathVariable("version") Double version, @RequestBody AppletCodeParameter param);
}
