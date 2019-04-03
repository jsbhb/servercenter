package com.zm.goods.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.bussiness.service.GoodsFeignService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.CustomCompletion;
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
			@RequestBody List<GoodsItemBO> list) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.manualOrderGoodsCheck(list);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	/**
	 * @fun 补全订单申报时商品信息
	 * @param version
	 * @param itemIdList
	 * @return
	 */
	@RequestMapping(value = "{version}/goods/feign/custom/completion", method = RequestMethod.POST)
	public List<CustomCompletion> customCompletion(@PathVariable("version") Double version,
			@RequestBody List<String> itemIdList) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return goodsFeignService.customCompletion(itemIdList);
		}
		return null;
	}
}
