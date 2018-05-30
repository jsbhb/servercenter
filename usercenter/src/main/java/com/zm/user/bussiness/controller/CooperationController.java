package com.zm.user.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.user.bussiness.service.CooperationService;
import com.zm.user.common.Pagination;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.po.PartnerPO;
import com.zm.user.pojo.po.ShopKeeperPO;

@RestController
public class CooperationController {

	@Resource
	CooperationService cooperationService;

	@RequestMapping(value = "auth/{version}/cooperation/list/shopkeeper", method = RequestMethod.GET)
	public ResultModel listShopkeeper(@PathVariable("version") Double version, @ModelAttribute Pagination pagination) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return cooperationService.listShopkeeper(pagination);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "auth/{version}/cooperation/list/partner", method = RequestMethod.GET)
	public ResultModel listPartner(@PathVariable("version") Double version, @ModelAttribute Pagination pagination) {
		if (Constants.FIRST_VERSION.equals(version)) {

			return cooperationService.listPartner(pagination);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/partner/save", method = RequestMethod.POST)
	public ResultModel savePartner(@PathVariable("version") Double version, @RequestBody PartnerPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.savePartner(po);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/shopkeeper/save", method = RequestMethod.POST)
	public ResultModel saveShopkeeper(@PathVariable("version") Double version, @RequestBody ShopKeeperPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.saveShopkeeper(po);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/shopkeeper/update", method = RequestMethod.POST)
	public ResultModel updateShopkeeper(@PathVariable("version") Double version, @RequestBody ShopKeeperPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.updateShopkeeper(po);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/partner/update", method = RequestMethod.POST)
	public ResultModel updatePartner(@PathVariable("version") Double version, @RequestBody PartnerPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.updatePartner(po);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/shopkeeper/delete/{id}", method = RequestMethod.DELETE)
	public ResultModel deleteShopkeeper(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.deleteShopkeeper(id);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/partner/delete/{id}", method = RequestMethod.DELETE)
	public ResultModel deletePartner(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.deletePartner(id);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/shopkeeper", method = RequestMethod.POST)
	public ResultModel listShopkeeperBack(@PathVariable("version") Double version,
			@RequestBody ShopKeeperPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.listShopkeeperBack(po);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/partner", method = RequestMethod.POST)
	public ResultModel listPartnerBack(@PathVariable("version") Double version, @RequestBody PartnerPO po) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.listPartnerBack(po);
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/cooperation/shopkeeper/{id}", method = RequestMethod.GET)
	public ResultModel getShopkeeperBack(@PathVariable("version") Double version,
			@PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.getShopkeeperBack(id);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/cooperation/partner/{id}", method = RequestMethod.GET)
	public ResultModel getPartnerBack(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cooperationService.getPartnerBack(id);
		}

		return new ResultModel(false, "版本错误");
	}
}
