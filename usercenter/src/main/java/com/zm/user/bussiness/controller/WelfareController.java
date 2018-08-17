package com.zm.user.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.user.bussiness.service.WelfareService;
import com.zm.user.common.Pagination;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.InviterEntity;

@RestController
public class WelfareController {

	@Resource
	WelfareService welfareService;

	@RequestMapping(value = "{version}/welfare/inviter/import", method = RequestMethod.POST)
	public ResultModel importInviterInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody List<InviterEntity> importList) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return welfareService.ImportInviterList(importList);
			} else {
				return new ResultModel(false, "版本错误");
			}
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/welfare/inviter/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody InviterEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				Page<InviterEntity> page = welfareService.queryForPage(entity);
				return new ResultModel(true, page, new Pagination(page));
			} else {
				return new ResultModel(false, "版本错误");
			}
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/welfare/inviter/update", method = RequestMethod.POST)
	public ResultModel updateInviter(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody InviterEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return welfareService.updateInviter(entity);
			} else {
				return new ResultModel(false, "版本错误");
			}
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/welfare/inviter/produceCode", method = RequestMethod.POST)
	public ResultModel produceCode(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody InviterEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return welfareService.produceCode(entity);
			} else {
				return new ResultModel(false, "版本错误");
			}
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/welfare/inviter/sendProduceCode", method = RequestMethod.POST)
	public ResultModel sendProduceCode(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody InviterEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return welfareService.sendProduceCode(entity);
			} else {
				return new ResultModel(false, "版本错误");
			}
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/welfare/inviter/statistic/{gradeId}", method = RequestMethod.GET)
	public ResultModel inviterStatistic(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("gradeId") Integer gradeId) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return welfareService.inviterStatistic(gradeId);
			} else {
				return new ResultModel(false, "版本错误");
			}
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
}
