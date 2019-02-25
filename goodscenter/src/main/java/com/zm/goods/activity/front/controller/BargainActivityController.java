package com.zm.goods.activity.front.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.activity.front.service.BargainActivityService;
import com.zm.goods.activity.model.bargain.dto.BargainInfoDTO;
import com.zm.goods.activity.model.bargain.vo.MyBargain;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.exception.ActiviteyException;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.DealOrderDataBO;

@RestController
public class BargainActivityController {

	@Resource
	BargainActivityService bargainActivityService;

	/**
	 * @fun 获取我的砍价活动
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/mine/{userId}", method = RequestMethod.GET)
	public ResultModel listMyBargain(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@RequestParam(value = "start", required = false) Integer start) {
		if (Constants.FIRST_VERSION.equals(version)) {
			List<MyBargain> myBargainList = bargainActivityService.listMyBargain(userId, start);
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
	public ResultModel userBargainOver(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("id") Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			boolean flag = bargainActivityService.userBargainOver(userId, id);
			return new ResultModel(flag, null);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 获取单个商品的砍价详情
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/active/bargain/mine/{id}", method = RequestMethod.GET)
	public ResultModel getMySingleBargainDetail(@PathVariable("version") Double version,
			@PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			MyBargain myBargain;
			try {
				myBargain = bargainActivityService.getMyBargainDetail(id);
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
	@RequestMapping(value = "auth/{version}/active/bargain/goods/list", method = RequestMethod.GET)
	public ResultModel listBargainGoods(@PathVariable("version") Double version,
			@RequestParam(value = "userId", required = false) Integer userId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> result = bargainActivityService.listBargainGoods(userId);
			return new ResultModel(true, result);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 开始一个砍价团,并砍第一刀
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/start", method = RequestMethod.POST)
	public ResultModel startBargain(@PathVariable("version") Double version, @RequestBody BargainInfoDTO dto) {

		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				Integer id = bargainActivityService.startBargain(dto);
				return new ResultModel(true, id);
			} catch (ActiviteyException e) {
				return new ResultModel(false, e.getErrorCode() + "", e.getMessage());
			}
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 砍价
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain", method = RequestMethod.POST)
	public ResultModel bargain(@PathVariable("version") Double version, @RequestBody BargainInfoDTO dto) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				double bargainPrice = bargainActivityService.bargain(dto);
				return new ResultModel(true, bargainPrice);
			} catch (ActiviteyException e) {
				return new ResultModel(false, e.getErrorCode() + "", e.getMessage());
			}
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 砍价订单获取商品价格等信息
	 */
	@RequestMapping(value = "{version}/active/bargain/goods/info", method = RequestMethod.POST)
	public ResultModel getBargainGoodsInfo(@PathVariable("version") Double version,
			@RequestBody DealOrderDataBO bo, Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return bargainActivityService.getBargainGoodsInfo(bo, id);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	/**
	 * @fun 订单生成后，该用户置为已购买
	 */
	@RequestMapping(value = "{version}/active/bargain/goods/buy", method = RequestMethod.POST)
	public boolean updateBargainGoodsBuy(@PathVariable("version") Double version, Integer userId, Integer id) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return bargainActivityService.updateBargainGoodsBuy(userId, id);
		}
		return false;
	}

	/**
	 * @fun 重新开始砍价
	 * @param version
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "{version}/active/bargain/retry", method = RequestMethod.POST)
	public ResultModel bargainRetry(@PathVariable("version") Double version, @RequestBody BargainInfoDTO dto) {
		if (Constants.FIRST_VERSION.equals(version)) {
			try {
				return bargainActivityService.bargainRetry(dto);
			} catch (ActiviteyException e) {
				return new ResultModel(false, e.getErrorCode()+"", e.getMessage());
			}
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

}
