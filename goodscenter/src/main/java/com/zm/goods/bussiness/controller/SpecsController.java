package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.bussiness.service.SpecsService;
import com.zm.goods.common.Pagination;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.SpecsEntity;
import com.zm.goods.pojo.SpecsTemplateEntity;
import com.zm.goods.pojo.SpecsValueEntity;
import com.zm.goods.pojo.bo.GoodsSpecsBO;

/**
 * ClassName: SpecsController <br/>
 * Function: 规格接口. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class SpecsController {

	@Resource
	SpecsService specsService;

	@RequestMapping(value = "{version}/goods/specs/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody SpecsTemplateEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<SpecsTemplateEntity> page = specsService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/specs/save", method = RequestMethod.POST)
	public ResultModel save(@PathVariable("version") Double version, @RequestBody SpecsTemplateEntity template) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				specsService.save(template);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/specs/saveSpecs", method = RequestMethod.POST)
	public ResultModel saveSpecs(@PathVariable("version") Double version, @RequestBody SpecsEntity entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				specsService.saveSpecs(entity);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/specs/saveValue", method = RequestMethod.POST)
	public ResultModel saveValue(@PathVariable("version") Double version, @RequestBody SpecsValueEntity value) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				specsService.saveValue(value);
				return new ResultModel(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/goods/specs/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody SpecsTemplateEntity entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getId() == 0) {
					return new ResultModel(false, "没有编号信息");
				}

				SpecsTemplateEntity result = specsService.queryById(entity.getId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/goods/specs/queryAll", method = RequestMethod.POST)
	public ResultModel queryAll(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				List<SpecsTemplateEntity> result = specsService.queryAll();
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/goods/specs/selectAllSpece", method = RequestMethod.GET)
	public ResultModel selectAllSpece(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return new ResultModel(true, specsService.selectAllSpece());
			}
			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/goods/specs/selectAllSpeceValue", method = RequestMethod.GET)
	public ResultModel selectAllSpeceValue(HttpServletRequest request, @PathVariable("version") Double version) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				return new ResultModel(true, specsService.selectAllSpeceValue());
			}
			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
	
	@RequestMapping(value = "{version}/goods/specs/save/new", method = RequestMethod.POST)
	public ResultModel addSpecs(@PathVariable("version") Double version, @RequestBody GoodsSpecsBO entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, specsService.addSpecs(entity));
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/goods/specs/value/save", method = RequestMethod.POST)
	public ResultModel addSpecsValue(@PathVariable("version") Double version, @RequestBody GoodsSpecsBO entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, specsService.addSpecsValue(entity));
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultModel(false, e.getMessage());
			}
		}
		return new ResultModel(false, "版本错误");
	}
}
