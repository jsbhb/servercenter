package com.zm.order.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.order.bussiness.service.ExpressService;
import com.zm.order.common.Pagination;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.bo.ExpressTemplateBO;

@RestController
public class ExpressController {

	@Resource
	ExpressService expressService;
	
	@RequestMapping(value = "{version}/express/template/list", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version,@RequestBody ExpressTemplateBO template) {
		if (Constants.FIRST_VERSION.equals(version)) {
			Page<ExpressTemplateBO> page = expressService.queryForPage(template);
			return new ResultModel(true, page, new Pagination(page));
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/express/template/enable/{id}", method = RequestMethod.POST)
	public ResultModel enable(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			expressService.enable(id);
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/save", method = RequestMethod.POST)
	public ResultModel saveExpressTemplate(@PathVariable("version") Double version, @RequestBody ExpressTemplateBO template) {
		if (Constants.FIRST_VERSION.equals(version)) {
			expressService.saveExpressTemplate(template);
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/update", method = RequestMethod.POST)
	public ResultModel updateExpressTemplate(@PathVariable("version") Double version, @RequestBody ExpressTemplateBO template) {
		if (Constants.FIRST_VERSION.equals(version)) {
			expressService.updateExpressTemplate(template);
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/{id}", method = RequestMethod.GET)
	public ResultModel getExpressTemplate(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, expressService.getExpressTemplate(id));
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
