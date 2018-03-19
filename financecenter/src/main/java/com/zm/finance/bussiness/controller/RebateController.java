package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.finance.bussiness.service.RebateService;
import com.zm.finance.constants.Constants;

@RestController
public class RebateController {

	@Resource
	RebateService rebateService;

	@RequestMapping(value = "{version}/finance/rebate", method = RequestMethod.POST)
	public void updateRebateTask(@PathVariable("version") Double version){
		
		if(Constants.FIRST_VERSION.equals(version)){
			rebateService.updateRebateTask();
		}
	}
}
