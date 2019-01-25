package com.zm.supplier.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.pojo.OrderInfo;

@FeignClient("3rdcenter")
public interface ThirdFeignClient {

	@RequestMapping(value = "{version}/express/createExpressInfoByExpressCode/{expressCode}", method = RequestMethod.POST)
	public ResultModel getExpressInfo(@PathVariable("version") Double version, @RequestBody List<OrderInfo> infoList,
			@PathVariable("expressCode") String expressCode);
}
