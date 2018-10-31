package com.zm.order.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderScheduleService;
import com.zm.order.constants.Constants;

@RestController
public class OrderScheduleController {

	@Resource
	OrderScheduleService orderScheduleService;
	
	@RequestMapping(value = "{version}/rebateorder/finance/schedule", method = RequestMethod.GET)
	public void saveRebateOrderToFinancecenter(@PathVariable("version") Double version){
		if(Constants.FIRST_VERSION.equals(version)){
			orderScheduleService.saveRebateOrderToFinancecenter();
		}
	}
}
