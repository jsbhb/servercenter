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
import com.zm.thirdcenter.utils.IPUtils;

@RestController
public class PageViewController {

	@Resource
	PageViewService pageViewService;

	@RequestMapping(value = "auth/{version}/pv/{pageName}/{type}", method = RequestMethod.POST)
	public ResultModel pageView(HttpServletRequest request, @PathVariable("version") Double version,
			@PathVariable("pageName") String pageName, @PathVariable("type") Integer type) {
		if (Constants.FIRST_VERSION.equals(version)) {
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
		if (Constants.FIRST_VERSION.equals(version)) {
			pageViewService.persistTask();
		}
	}

	/**
	 * @fun 获取IP
	 * @param version
	 */
	@RequestMapping(value = "auth/{version}/ip", method = RequestMethod.GET)
	public ResultModel getIp(@PathVariable("version") Double version, HttpServletRequest req) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(IPUtils.getOriginIp(req));
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "auth/{version}/flow/{shopId}", method = RequestMethod.GET)
	public void flowStatis(@PathVariable("version") Double version, @PathVariable("shopId") int shopId,
			HttpServletRequest request) {
		if (Constants.FIRST_VERSION.equals(version)) {
			pageViewService.flowStatis(request, shopId);
		}
	}
}
