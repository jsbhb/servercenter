package com.zm.thirdcenter.bussiness.info.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @fun 资讯类控制器
 * @author user
 *
 */

import com.zm.thirdcenter.bussiness.info.service.InfoService;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.NewsModel;
import com.zm.thirdcenter.pojo.Pagination;
import com.zm.thirdcenter.pojo.ResultModel;

@RestController
public class InfoController {

	@Resource
	InfoService infoService;

	@RequestMapping(value = "/{version}/info/news", method = RequestMethod.POST)
	public ResultModel saveNews(@PathVariable("version") Double version, @RequestBody NewsModel model) {
		if (Constants.FIRST_VERSION.equals(version)) {
			infoService.saveNews(model);
			return new ResultModel(null);
		}
		return new ResultModel(false, "版本号有误");
	}

	@RequestMapping(value = "/{version}/info/news/rand/{type}", method = RequestMethod.GET)
	public ResultModel listNewsRand(@PathVariable("version") Double version, @PathVariable("type") int type) {
		if (Constants.FIRST_VERSION.equals(version)) {
			List<NewsModel> newsList = infoService.listNewsRand(type);
			return new ResultModel(newsList);
		}
		return new ResultModel(false, "版本号有误");
	}

	@RequestMapping(value = "/auth/{version}/info/news/list/{type}", method = RequestMethod.GET)
	public ResultModel listNews(@PathVariable("version") Double version, @PathVariable("type") int type,
			@ModelAttribute Pagination pagination) {
		if (Constants.FIRST_VERSION.equals(version)) {
			List<NewsModel> newsList = infoService.listNews(type, pagination);
			return new ResultModel(newsList,pagination);
		}
		return new ResultModel(false, "版本号有误");
	}
}
