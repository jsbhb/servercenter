package com.zm.thirdcenter.bussiness.pageview.service;

import javax.servlet.http.HttpServletRequest;

public interface PageViewService {

	void pageviewStatistics(HttpServletRequest request, String pageName, Integer type);

	void persistTask();
}
