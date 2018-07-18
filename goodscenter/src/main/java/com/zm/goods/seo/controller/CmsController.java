package com.zm.goods.seo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.seo.service.SEOService;

/**
 * @fun 用于提供前端服务器需求数据
 * @author user
 *
 */
@RestController
public class CmsController {

	@Resource
	SEOService seoService;

	/**
	 * @fun 发布首页
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/cms/index/{id}", method = RequestMethod.GET)
	public ResultModel retrievePage(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return new ResultModel(true, seoService.retrievePage(id));
			} catch (Exception e) {
				return new ResultModel(false, e.getMessage());
			}
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
