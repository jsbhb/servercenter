package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.user.common.ResultModel;


@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "{version}/goods/table/{centerId}", method = RequestMethod.POST)
	public ResultModel createTable(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId);
}
