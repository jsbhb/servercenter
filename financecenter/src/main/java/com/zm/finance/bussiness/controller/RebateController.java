package com.zm.finance.bussiness.controller;

import java.util.Map;

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
import com.zm.finance.pojo.RebateSearchModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.rebate.Rebate;
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

	@RequestMapping(value = "{version}/finance/rebate/{gradeId}", method = RequestMethod.GET)
	public ResultModel getRebate(@PathVariable("version") Double version, @PathVariable("gradeId") Integer gradeId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return rebateService.getRebate(gradeId);
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

	@RequestMapping(value = "{version}/finance/rebate/list", method = RequestMethod.POST)
	public ResultModel listRebate(@PathVariable("version") Double version, @RequestBody RebateSearchModel search) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<Rebate> page = rebateService.listRebate(search);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/finance/rebate/detail/save", method = RequestMethod.POST)
	public void saveRebateDetail(@PathVariable("version") Double version, @RequestBody Map<String, String> map) {

		if (Constants.FIRST_VERSION.equals(version)) {
			rebateService.saveRebateDetail(map);
		}
	}

	@RequestMapping(value = "{version}/finance/rebate/detail/finsh", method = RequestMethod.POST)
	public void updateRebateDetail(@PathVariable("version") Double version, @RequestParam("orderId") String orderId,
			@RequestParam("status") Integer status) {

		if (Constants.FIRST_VERSION.equals(version)) {
			rebateService.updateRebateDetail(orderId, status);
		}
	}

	// 操作redis的小工具，没有其他作用
	@RequestMapping(value = "{version}/finance/rebate/detail/redis", method = RequestMethod.POST)
	public void redisTool(@PathVariable("version") Double version, @RequestParam("key") String key,
			@RequestBody Map<String, String> map) {

		if (Constants.FIRST_VERSION.equals(version)) {
			rebateService.redisTool(key, map);
		}
	}

	@RequestMapping(value = "{version}/finance/rebate/detail/redis/set", method = RequestMethod.POST)
	public void redisToolList(@PathVariable("version") Double version, @RequestParam("key") String key,
			@RequestParam("value") String value) {

		if (Constants.FIRST_VERSION.equals(version)) {
			rebateService.redisToolList(key, value);
		}
	}

}
