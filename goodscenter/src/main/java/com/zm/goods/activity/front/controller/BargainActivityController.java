package com.zm.goods.activity.front.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.goods.activity.front.service.BargainActivityService;
import com.zm.goods.activity.model.bargain.vo.BargainGoods;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.common.Pagination;

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
	@RequestMapping(value = "{version}/active/bargain/over/{userId}/{id}", method = RequestMethod.PUT)
	public ResultModel userBargainOver(@PathVariable("version") Double version,
			@PathVariable("userId") Integer userId, @PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			boolean flag = bargainActivityService.userBargainOver(userId, id);
			return new ResultModel(flag,null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 获取单个商品的砍价详情
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/mine/{userId}/{id}", method = RequestMethod.GET)
	public ResultModel getMySingleBargainDetail(@PathVariable("version") Double version,
			@PathVariable("userId") Integer userId, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			MyBargain myBargain;
			try {
				myBargain = bargainActivityService.getMyBargainDetail(userId, id);
				return new ResultModel(true, myBargain);
			} catch (ActiviteyException e) {
				return new ResultModel(false, e.getErrorCode() + "", e.getMessage());
			}
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 获取砍价商品列表
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/goods/list", method = RequestMethod.GET)
	public ResultModel listBargainGoods(@PathVariable("version") Double version,
			@ModelAttribute Pagination pagination) {
		if (Constants.FIRST_VERSION.equals(version)) {
			Page<BargainGoods> page = bargainActivityService.listBargainGoods(pagination);
			return new ResultModel(true, page, new Pagination(page));
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 开始一个砍价团
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/start/{userId}/{goodsRoleId}", method = RequestMethod.POST)
	public ResultModel startBargain(@PathVariable("version") Double version,
			@PathVariable("userId") Integer userId, @PathVariable("goodsRoleId") Integer goodsRoleId) {
		
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				Integer id = bargainActivityService.startBargain(userId,goodsRoleId);
				return new ResultModel(true, id);
			} catch (ActiviteyException e) {
				return new ResultModel(false, e.getErrorCode()+"", e.getMessage());
			}
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 砍价
	 * @return
	 */
	public ResultModel bargain(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
