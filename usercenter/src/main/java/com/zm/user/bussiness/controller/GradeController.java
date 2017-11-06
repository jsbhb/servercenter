/**  
 * Project Name:usercenter  
 * File Name:GradeController.java  
 * Package Name:com.zm.user.bussiness.controller  
 * Date:Oct 29, 20177:43:20 PM  
 *  
 */
package com.zm.user.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.user.bussiness.service.GradeService;
import com.zm.user.common.Pagination;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ResultModel;

/**
 * ClassName: GradeController <br/>
 * Function: 分级管理. <br/>
 * date: Oct 29, 2017 7:43:20 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@RestController
public class GradeController {

	@Resource
	GradeService gradeService;

	@RequestMapping(value = "{version}/grade/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody Grade grade) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {

				Page<Grade> grades = gradeService.queryForPagination(grade);
				return new ResultModel(true, grades, new Pagination(grades));
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/grade/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody Grade grade) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (grade.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				Grade result = gradeService.queryById(grade.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

}
