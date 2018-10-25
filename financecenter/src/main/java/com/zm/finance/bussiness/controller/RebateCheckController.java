package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.finance.bussiness.service.RebateCheckService;
import com.zm.finance.constants.Constants;

/**
 * @fun 返佣对账类
 * @author user
 *
 */
@RestController
public class RebateCheckController {
	
	@Resource
	RebateCheckService rebateCheckService;

	@RequestMapping(value="{version}/rebate/check", method = RequestMethod.POST)
	public void rebateCheck(@PathVariable("version") Double version){
		if (Constants.FIRST_VERSION.equals(version)) {
			rebateCheckService.rebateCheck();
		}
	}
}
