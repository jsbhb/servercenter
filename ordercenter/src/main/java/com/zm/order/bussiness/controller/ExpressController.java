package com.zm.order.bussiness.controller;

import java.util.List;

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
import com.zm.order.exception.ParameterException;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.bo.ExpressTemplateBO;
import com.zm.order.pojo.po.ExpressRulePO;
import com.zm.order.pojo.po.RuleParameterPO;

@RestController
public class ExpressController {

	@Resource
	ExpressService expressService;

	@RequestMapping(value = "{version}/express/template/list", method = RequestMethod.POST)
	public ResultModel queryForPage(@PathVariable("version") Double version, @RequestBody ExpressTemplateBO template) {
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
	public ResultModel saveExpressTemplate(@PathVariable("version") Double version,
			@RequestBody ExpressTemplateBO template) {
		if (Constants.FIRST_VERSION.equals(version)) {
			expressService.saveExpressTemplate(template);
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/express/template/update", method = RequestMethod.POST)
	public ResultModel updateExpressTemplate(@PathVariable("version") Double version,
			@RequestBody ExpressTemplateBO template) {
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

	@RequestMapping(value = "{version}/express/template/express-fee/{id}", method = RequestMethod.DELETE)
	public ResultModel delExpressFee(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			expressService.delExpressFee(id);
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/rulebind/{id}", method = RequestMethod.DELETE)
	public ResultModel delRuleBind(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			expressService.delRuleBind(id);
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/rule", method = RequestMethod.GET)
	public ResultModel listRule(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			List<ExpressRulePO> list = expressService.listRule();
			return new ResultModel(true, list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/rule/param/{id}", method = RequestMethod.GET)
	public ResultModel listRuleParam(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			List<RuleParameterPO> list = expressService.listRuleParam(id);
			return new ResultModel(true, list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/express/template/rule/param", method = RequestMethod.POST)
	public ResultModel addRuleParam(@PathVariable("version") Double version, @RequestBody RuleParameterPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				expressService.addRuleParam(po);
			} catch (ParameterException e) {
				return new ResultModel(false, e.getMessage());
			}
			return new ResultModel(true, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
