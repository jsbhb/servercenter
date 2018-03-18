package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.finance.bussiness.service.CapitalPoolService;
import com.zm.finance.constants.Constants;

@RestController
public class CapitalPoolController {
	
	@Resource
	CapitalPoolService capitalPoolService;

	@RequestMapping(value = "{version}/finance/calCapitalPool", method = RequestMethod.POST)
	public void updateCapitalPoolTask(@PathVariable("version") Double version){
		
		if(Constants.FIRST_VERSION.equals(version)){
			capitalPoolService.updateCapitalPoolTask();
		}
	}
}
