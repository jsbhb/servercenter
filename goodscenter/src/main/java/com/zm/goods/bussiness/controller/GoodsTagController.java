package com.zm.goods.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsTagService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;

@RestController
public class GoodsTagController {
	
	@Resource
	GoodsTagService GoodsTagService;

	@RequestMapping(value = "{version}/goods/tag", method = RequestMethod.DELETE)
	public ResultModel deleteTag(@PathVariable("version") Double version, @RequestParam("tagId") Integer tagId,
			@RequestParam("bingId") Integer bingId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
