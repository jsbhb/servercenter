package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.finance.bussiness.service.RebateService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.Pagination;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.rebate.RebateDetail;

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
	
	@RequestMapping(value = "{version}/finance/rebate/detail", method = RequestMethod.POST)
	public ResultModel getRebateDetail(@PathVariable("version") Double version, @RequestBody RebateDetail entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<RebateDetail> page = rebateService.getRebateDetail(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
}
