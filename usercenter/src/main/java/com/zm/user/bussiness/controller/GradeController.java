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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.user.bussiness.service.GradeService;
import com.zm.user.bussiness.service.UserService;
import com.zm.user.common.Pagination;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.log.LogUtil;
import com.zm.user.pojo.FuzzySearchGrade;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ShopEntity;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.po.GradeTypePO;
import com.zm.user.pojo.po.RebateFormula;

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

	@Resource
	UserService userService;

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
			@RequestBody Grade pagination) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (pagination.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				Grade result = gradeService.queryById(pagination.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/grade/update", method = RequestMethod.POST)
	public ResultModel update(@PathVariable("version") Double version, @RequestBody Grade entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有分级编号");

				}
				gradeService.update(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				LogUtil.writeErrorLog("更新分级出错", e);
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/shop/query", method = RequestMethod.POST)
	public ResultModel queryByGradeId(@PathVariable("version") Double version, @RequestBody ShopEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				if (entity.getGradeId() == 0) {
					return new ResultModel(false, "没有分级编号");

				}
				ShopEntity result = gradeService.queryByGradeId(entity.getGradeId());
				return new ResultModel(true, result);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/shop/userInfo/query/{needPaging}", method = RequestMethod.POST)
	public ResultModel queryUserInfoByShopId(@PathVariable("version") Double version,
			@PathVariable("needPaging") boolean needPaging, @RequestBody UserInfo entity) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return userService.getAllUserInfoForShopByParam(needPaging, entity);
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/shop/update", method = RequestMethod.POST)
	public ResultModel updateShop(@PathVariable("version") Double version, @RequestBody ShopEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				if (entity.getGradeId() == 0) {
					return new ResultModel(false, "没有分级编号");

				}
				gradeService.updateShop(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "auth/{version}/grade/fuzzy-search", method = RequestMethod.POST)
	public ResultModel fuzzySearch(@PathVariable("version") Double version, @RequestBody FuzzySearchGrade entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.fuzzySearch(entity);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/type", method = RequestMethod.POST)
	public ResultModel saveGradeType(@PathVariable("version") Double version, @RequestBody GradeTypePO entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.saveGradeType(entity);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/type", method = RequestMethod.GET)
	public ResultModel listGradeType(@PathVariable("version") Double version,
			@RequestParam(value = "id", required = false) Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.listGradeType(id);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/type/{id}", method = RequestMethod.GET)
	public ResultModel getGradeType(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.getGradeType(id);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/type/children", method = RequestMethod.GET)
	public ResultModel listGradeTypeChildren(@PathVariable("version") Double version,
			@RequestParam(value = "id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.listGradeTypeChildren(id);
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/grade/type/children/gradeId", method = RequestMethod.GET)
	public ResultModel listGradeTypeChildrenByParentGradeId(@PathVariable("version") Double version,
			@RequestParam(value = "gradeId") Integer gradeId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.listGradeTypeChildrenByParentGradeId(gradeId);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/type", method = RequestMethod.DELETE)
	public ResultModel removeGradeType(@PathVariable("version") Double version,
			@RequestParam(value = "id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.removeGradeType(id);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/type/update", method = RequestMethod.POST)
	public ResultModel updateGradeType(@PathVariable("version") Double version, @RequestBody GradeTypePO entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.updateGradeType(entity);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/area-center/init/{id}", method = RequestMethod.POST)
	public ResultModel initAreaCenter(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.initAreaCenter(id);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/updateWelfareType", method = RequestMethod.POST)
	public ResultModel updateWelfareType(@PathVariable("version") Double version, @RequestBody Grade entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有分级编号");

				}
				gradeService.updateWelfareType(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				LogUtil.writeErrorLog("更新福利类型出错", e);
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/rebate/formula", method = RequestMethod.POST)
	public ResultModel saveGradeTypeRebateFormula(@PathVariable("version") Double version,
			@RequestBody RebateFormula rebateFormula) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.saveGradeTypeRebateFormula(rebateFormula);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/rebate/formula/update", method = RequestMethod.POST)
	public ResultModel updateGradeTypeRebateFormula(@PathVariable("version") Double version,
			@RequestBody RebateFormula rebateFormula) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.updateGradeTypeRebateFormula(rebateFormula);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/rebate/formula/{needPaging}", method = RequestMethod.POST)
	public ResultModel listGradeTypeRebateFormula(@PathVariable("version") Double version,
			@PathVariable("needPaging") boolean needPaging, @RequestBody RebateFormula rebateFormula) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.listGradeTypeRebateFormula(rebateFormula, needPaging);
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/grade/rebate/formula/{id}", method = RequestMethod.GET)
	public ResultModel getGradeTypeRebateFormulaById(@PathVariable("version") Double version,
			@PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.getGradeTypeRebateFormulaById(id);
		}
		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 审核微店
	 */
	@RequestMapping(value = "{version}/shop/audit", method = RequestMethod.POST)
	public ResultModel auditShop(@PathVariable("version") Double version, @RequestBody Grade grade) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return gradeService.auditShop(grade);
		}
		return new ResultModel(false, "版本错误");
	}
}
