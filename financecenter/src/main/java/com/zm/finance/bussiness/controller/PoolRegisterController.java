package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.finance.bussiness.service.CapitalPoolService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.ResultModel;

@RestController
public class PoolRegisterController {

	@Resource
	CapitalPoolService capitalPoolService;

	@RequestMapping(value = "{version}/finance/capitalpool/register/{centerId}", method = RequestMethod.POST)
	public ResultModel CapitalPoolRecharge(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.CapitalPoolRecordRegister(centerId);
		}
		return new ResultModel(false, "版本错误");
	}
}
