package com.zm.finance.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.finance.bussiness.service.WithdrawalsService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.Pagination;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.withdrawals.Withdrawals;

@RestController
public class WithdrawalsController {

	@Resource
	WithdrawalsService withdrawalsService;

	@RequestMapping(value = "{version}/finance/withdrawal/apply", method = RequestMethod.POST)
	public ResultModel withdrawalsApply(@PathVariable("version") Double version, @RequestBody Withdrawals withdrawals) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return withdrawalsService.withdrawalsApply(withdrawals);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/withdrawal/audit", method = RequestMethod.POST)
	public ResultModel withdrawalAudit(@PathVariable("version") Double version, @RequestBody AuditModel audit) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return withdrawalsService.withdrawalAudit(audit);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/withdrawal/detail/{gradeId}", method = RequestMethod.GET)
	public ResultModel listWithdrawal(@PathVariable("version") Double version, @PathVariable("gradeId") Integer gradeId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return withdrawalsService.getWithdrawal(gradeId);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/finance/withdrawal/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody Withdrawals entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<Withdrawals> page = withdrawalsService.queryForPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/withdrawal/detailById/{id}", method = RequestMethod.POST)
	public ResultModel getWithdrawal(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, withdrawalsService.queryWithdrawalsDetailById(id));
		}
		return new ResultModel(false, "版本错误");
	}
}
