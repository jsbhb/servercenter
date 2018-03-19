package com.zm.timetask.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("financecenter")
public interface FinanceFeignClient {

	@RequestMapping(value = "{version}/finance/calCapitalPool", method = RequestMethod.POST)
	public void updateCapitalPoolTask(@PathVariable("version") Double version);
	
	@RequestMapping(value = "{version}/finance/rebate", method = RequestMethod.POST)
	public void updateRebateTask(@PathVariable("version") Double version);
}
