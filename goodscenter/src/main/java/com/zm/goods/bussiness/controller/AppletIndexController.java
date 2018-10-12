package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.po.PagePO;
import com.zm.goods.seo.model.SEODetail;
import com.zm.goods.seo.service.SEOService;

@RestController
public class AppletIndexController {

	@Resource
	SEOService seoService;

	/**
	 * @fun 获取小程序首页数据，和微店一样（微店是发布形式）
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/applet/index/{id}", method = RequestMethod.GET)
	public SEODetail getAppletIndexData(@PathVariable("version") Double version, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			// 检索数据
			PagePO pagePo = seoService.retrievePageData(id);
			// 数据转化为接口结构
			SEODetail seoDetail = seoService.convertToSEODetail(pagePo);
			return seoDetail;
		}
		return null;
	}
}
