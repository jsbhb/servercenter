package com.zm.goods.activity.backmanger.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.activity.backmanger.model.BargainActivityModel;
import com.zm.goods.activity.backmanger.model.BaseActivityModel;
import com.zm.goods.activity.backmanger.service.BackBargainActivityService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ResultModel;

@RestController
public class ActivityBackManagerController {

	@Resource
	BackBargainActivityService backBargainActivityService;
	
	
	@RequestMapping(value = "{version}/activity/backManager/queryActivityForPage", method = RequestMethod.POST)
	public ResultModel queryActivityForPage(@PathVariable("version") Double version, @RequestBody BaseActivityModel model) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				Page<BaseActivityModel> page = backBargainActivityService.queryActivityForPage(model);
				return new ResultModel(true, page, new Pagination(page));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/activity/backManager/saveBargainActivityInfo", method = RequestMethod.POST)
	public ResultModel saveBargainActivityInfo(@PathVariable("version") Double version, @RequestBody BargainActivityModel model) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				backBargainActivityService.insertBargainActivityInfo(model);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/activity/backManager/queryBargainActivityByParam", method = RequestMethod.POST)
	public ResultModel queryBargainActivityByParam(@PathVariable("version") Double version, @RequestBody BargainActivityModel model) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, backBargainActivityService.queryBargainActivityByParam(model));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/activity/backManager/modifyBargainActivityInfo", method = RequestMethod.POST)
	public ResultModel modifyBargainActivityInfo(@PathVariable("version") Double version, @RequestBody BargainActivityModel model) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				backBargainActivityService.modifyBargainActivityInfo(model);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/activity/backManager/queryBargainActivityShowPageInfo", method = RequestMethod.POST)
	public ResultModel queryBargainActivityShowPageInfo(@PathVariable("version") Double version, @RequestBody BargainActivityModel model) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, backBargainActivityService.queryBargainActivityShowPageInfo(model));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
}
