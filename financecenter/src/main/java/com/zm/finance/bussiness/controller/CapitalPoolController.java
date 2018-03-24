package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.finance.bussiness.service.CapitalPoolService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.Pagination;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.pojo.refilling.Refilling;

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
			return capitalPoolService.CapitalPoolRecharge(payNo, money, centerId);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/rebate/recharge/audit", method = RequestMethod.POST)
	public ResultModel CapitalPoolRechargeAudit(@PathVariable("version") Double version,
			@RequestBody AuditModel audit) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.CapitalPoolRechargeAudit(audit);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/listcalCapitalPool", method = RequestMethod.POST)
	public ResultModel listcalCapitalPool(@PathVariable("version") Double version,
			@RequestBody CapitalPool capitalPool) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.listcalCapitalPool(capitalPool);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/capital/recharge/apply/{centerId}", method = RequestMethod.POST)
	public ResultModel reChargeCapitalApply(@PathVariable("version") Double version, @RequestBody Refilling refilling) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.reChargeCapitalApply(refilling);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/capital/recharge/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody Refilling entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<Refilling> page = capitalPoolService.queryForPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/refilling/detailById/{id}", method = RequestMethod.POST)
	public ResultModel getWithdrawal(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.queryRefillingDetailById(id);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/capitalpool/liquidation/{centerId}", method = RequestMethod.POST)
	public ResultModel liquidation(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestParam("money") Double money) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return capitalPoolService.liquidation(centerId, money);
		}
		return new ResultModel(false, "版本错误");
	}
}
