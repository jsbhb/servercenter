package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.finance.bussiness.service.RebateService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.ResultModel;

@RestController
public class RebateController {

	@Resource
	RebateService rebateService;

	@RequestMapping(value = "{version}/finance/rebate", method = RequestMethod.POST)
	public void updateRebateTask(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			rebateService.updateRebateTask();
		}
	}

	@RequestMapping(value = "{version}/finance/rebate/{id}", method = RequestMethod.GET)
	public ResultModel getRebate(@PathVariable("version") Double version, @PathVariable("id") Integer id,
			@RequestParam("type") Integer type) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return rebateService.getRebate(id, type);
		}
		return new ResultModel(false, "版本错误");
	}
}
