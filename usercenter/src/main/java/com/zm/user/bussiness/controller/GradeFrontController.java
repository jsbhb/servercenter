package com.zm.user.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;

@RestController
public class GradeFrontController {

	@Resource
	GradeFrontService gradeFrontService;
	
	/**
	 * @fun 获取微店头部信息
	 * @param version
	 * @param gradeId
	 * @return
	 */
	@RequestMapping(value = "{version}/grade/config/{gradeId}", method = RequestMethod.GET)
	public ResultModel getGradeConfig(@PathVariable("version") Double version,@PathVariable("gradeId") Integer gradeId){
		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, gradeFrontService.getGradeConfig(gradeId));
		}
		return new ResultModel(false, "版本错误");
	}
}
