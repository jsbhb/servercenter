package com.zm.supplier.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.pojo.SendOrderResult;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	@RequestMapping(value = "{version}/order/saveThirdOrder", method = RequestMethod.POST)
	public ResultModel saveThirdOrder(@PathVariable("version") Double version,@RequestBody List<SendOrderResult> list);
}
