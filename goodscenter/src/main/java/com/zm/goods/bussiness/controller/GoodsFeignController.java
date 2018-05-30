package com.zm.goods.bussiness.controller;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.GoodsItemBO;

/**
 * @fun feign微服务内部调用控制器
 * @author user
 *
 */
@RestController
public class GoodsFeignController {

	@Resource
	GoodsFeignService goodsFeignService;

	@RequestMapping(value = "{version}/goods/feign/manualordergoods/check", method = RequestMethod.POST)
	public ResultModel manualOrderGoodsCheck(@PathVariable("version") Double version,
			@RequestBody Set<GoodsItemBO> set) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.manualOrderGoodsCheck(set);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
