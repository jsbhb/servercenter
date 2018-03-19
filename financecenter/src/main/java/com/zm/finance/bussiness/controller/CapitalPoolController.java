package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.finance.bussiness.service.CapitalPoolService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.ResultModel;

@RestController
public class CapitalPoolController {

	@Resource
	CapitalPoolService capitalPoolService;

	@RequestMapping(value = "{version}/finance/calCapitalPool", method = RequestMethod.POST)
	public void updateCapitalPoolTask(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			capitalPoolService.updateCapitalPoolTask();
		}
	}

	@RequestMapping(value = "{version}/finance/recharge/{centerId}", method = RequestMethod.POST)
	public ResultModel CapitalPoolRecharge(@PathVariable("version") Double version, @RequestParam("payNo") String payNo,
			@RequestParam("money") double money, @PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.CapitalPoolRecharge(payNo,money,centerId);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/rebate/recharge/{centerId}", method = RequestMethod.POST)
	public ResultModel CapitalPoolRecharge(@PathVariable("version") Double version,
			@RequestParam("money") double money, @PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.CapitalPoolRecharge(money,centerId);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/listcalCapitalPool", method = RequestMethod.GET)
	public ResultModel listcalCapitalPool(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.listcalCapitalPool();
		}
		return new ResultModel(false, "版本错误");
	}
}
