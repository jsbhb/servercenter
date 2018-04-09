package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsTagService;
import com.zm.goods.constants.Constants;

/**
 * @fun 标签控制器类，供feign使用，返回不使用ResultModel
 * @author user
 *
 */
@RestController
public class GoodsTagFeignController {
	
	@Resource
	GoodsTagService goodsTagService;

	@RequestMapping(value = "{version}/goods/tag/presell", method = RequestMethod.POST)
	public List<String> listPreSellItemIds(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsTagService.listPreSellItemIds();
		} 
		return null;
	}
}
