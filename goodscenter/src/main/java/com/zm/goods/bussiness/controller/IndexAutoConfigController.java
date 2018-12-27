package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.IndexAutoConfigService;
import com.zm.goods.constants.Constants;

@RestController
public class IndexAutoConfigController {

	@Resource
	IndexAutoConfigService indexAutoConfigService;

	@RequestMapping(value = "{version}/index/auto/config", method = RequestMethod.POST)
	public boolean indexAutoConfig(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)){
			return indexAutoConfigService.autoConfig();
		}
		return false;
	}
}
