package com.zm.thirdcenter.bussiness.pageview.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.bussiness.pageview.service.PageViewService;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.ResultModel;

@RestController
public class PageViewController {
	
	@Resource
	PageViewService pageViewService;

	@RequestMapping(value = "auth/{version}/pv/{pageName}/{type}", method = RequestMethod.POST)
	public ResultModel pageView(HttpServletRequest request, @PathVariable("version") Double version, @PathVariable("pageName") String pageName,
			@PathVariable("type") Integer type) {
		if(Constants.FIRST_VERSION.equals(version)){
			pageViewService.pageviewStatistics(request, pageName, type);
			return new ResultModel(true, "");
		}
		return new ResultModel(false, "版本错误");
	}
	
	/**
	 * @fun 落地处理
	 * @param version
	 */
	@RequestMapping(value = "{version}/pv/persist", method = RequestMethod.POST)
	public void persistTask(@PathVariable("version") Double version) {
		if(Constants.FIRST_VERSION.equals(version)){
			pageViewService.persistTask();
		}
	}
}
