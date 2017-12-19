package com.zm.activity.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.activity.bussiness.service.ActivityService;
import com.zm.activity.constants.Constants;
import com.zm.activity.pojo.ResultModel;

@RestController
public class ActivityController {
	
	@Resource
	ActivityService activityService;

	/**
	 * @fun 获取活动列表
	 * @param version
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/activity/{centerId}", method = RequestMethod.GET)
	public ResultModel listActivity(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, activityService.listActivity(centerId));
		}

		return new ResultModel(false, "版本错误");
	}
}
