package com.zm.pay.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.pay.feignclient.model.OrderBussinessModel;
import com.zm.pay.pojo.ResultModel;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "{version}/goods/stockjudge/{orderFlag}/{supplierId}", method = RequestMethod.POST)
	public ResultModel stockJudge(@PathVariable("version") Double version, @PathVariable("supplierId") Integer supplierId,
			@PathVariable("orderFlag") Integer orderFlag, @RequestBody List<OrderBussinessModel> list);
}
