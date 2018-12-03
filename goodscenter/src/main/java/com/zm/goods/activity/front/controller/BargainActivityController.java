package com.zm.goods.activity.front.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.activity.front.service.BargainActivityService;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.pojo.ResultModel;

@RestController
public class BargainActivityController {

	@Resource
	BargainActivityService bargainActivityService;
	/**
	 * @fun 获取我的砍价活动
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/mine/{userId}", method = RequestMethod.GET)
	public ResultModel listMyBargain(@PathVariable("version") Double version, @PathVariable("userId") Integer userId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			List<MyBargain> myBargainList = bargainActivityService.listMyBargain(userId);
			return new ResultModel(true, myBargainList);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 单个商品砍价时间到后，前端调用关闭砍价
	 * @return
	 */
	public ResultModel goodsBargainOver() {

	}

	/**
	 * @fun 获取单个商品的砍价详情
	 * @return
	 */
	public ResultModel getMySingleBargainDetail() {

	}

	/**
	 * @fun 获取砍价商品列表
	 * @return
	 */
	public ResultModel listBargainGoods() {

	}

	/**
	 * @fun 开始一个砍价团
	 * @return
	 */
	public ResultModel startBargain() {

	}

	/**
	 * @fun 砍价
	 * @return
	 */
	public ResultModel bargain() {

	}
}
