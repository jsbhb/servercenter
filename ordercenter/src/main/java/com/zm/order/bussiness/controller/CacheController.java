package com.zm.order.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.ResultModel;

@RestController
public class CacheController {

	@Resource
	CacheAbstractService cacheAbstractService;

	@RequestMapping(value = "{version}/cache", method = RequestMethod.GET)
	public String getCache(@PathVariable("version") Double version, @RequestParam("gradeId") Integer gradeId,
			@RequestParam("dataType") String dataType, @RequestParam("time") String time,
			@RequestParam("modelType") String modelType) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cacheAbstractService.getCache(gradeId, dataType, time, modelType);
		}
		return null;
	}

	@RequestMapping(value = "{version}/cache/day", method = RequestMethod.GET)
	public void saveDayCacheToWeek(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			cacheAbstractService.saveDayCacheToWeek();
		}
	}

	@RequestMapping(value = "{version}/cache/month", method = RequestMethod.GET)
	public void initMonth(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			cacheAbstractService.initMonth();
		}
	}

	/**
	 * @fun 前端店铺管理获取统计数据
	 * @param version
	 * @param gradeId
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "{version}/cache/shop/manager/statis", method = RequestMethod.GET)
	public ResultModel getShopManagerStatis(@PathVariable("version") Double version,
			@RequestParam("gradeId") Integer gradeId, @RequestParam("time") int time) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cacheAbstractService.getShopManagerStatis(gradeId, time);
		}
		return new ResultModel(false, "版本错误");
	}
	
	/**
	 * @fun 前端店铺管理获取统计数据
	 * @param version
	 * @param gradeId
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "{version}/cache/shop/manager/index/{gradeId}", method = RequestMethod.GET)
	public ResultModel getShopManagerIndex(@PathVariable("version") Double version,
			@PathVariable("gradeId") Integer gradeId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return cacheAbstractService.getShopManagerIndex(gradeId);
		}
		return new ResultModel(false, "版本错误");
	}
}
