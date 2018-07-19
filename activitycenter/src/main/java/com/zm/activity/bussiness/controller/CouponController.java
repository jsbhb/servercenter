package com.zm.activity.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.activity.bussiness.service.CouponService;
import com.zm.activity.constants.Constants;
import com.zm.activity.pojo.ResultModel;

/**
 * @fun 优惠券相关controller
 * @author user
 *
 */
@RestController
public class CouponController {

	@Resource
	CouponService couponService;

	/**
	 * @fun 获取优惠券列表
	 * @param version
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "auth/{version}/coupon/{centerId}", method = RequestMethod.GET)
	public ResultModel listCoupon(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "activityId") String activityId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, couponService.listCoupon(centerId, userId, activityId));
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 根据user获取优惠券列表
	 * @param version
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/coupon/{centerId}/{userId}", method = RequestMethod.GET)
	public ResultModel listCouponByCouponIds(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @PathVariable("userId") Integer userId,
			@RequestParam("status") Integer status) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, couponService.listCouponByUserId(centerId, userId, status));
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 领取优惠券
	 * @param version
	 * @param userId
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/receive-coupon/{centerId}", method = RequestMethod.GET)
	public ResultModel receiveCoupon(@PathVariable("version") Double version, @RequestParam("userId") Integer userId,
			@PathVariable("centerId") Integer centerId, @RequestParam("couponId") String couponId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return couponService.receiveCoupon(centerId, userId, couponId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 发放优惠券
	 * @param version
	 * @param List
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/giveout-coupon/{centerId}", method = RequestMethod.POST)
	public ResultModel giveOutCoupon(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestBody List<String> list) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return couponService.giveOutCoupon(centerId, list);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 根据goodsId获取优惠券（商品详情页显示）
	 * @param version
	 * @param List
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/coupon-goodsId/{centerId}", method = RequestMethod.GET)
	public ResultModel listCouponByGoodsId(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestParam("goodsId") String goodsId,
			@RequestParam(value = "firstId", required = false) String firstId,
			@RequestParam(value = "secondId", required = false) String secondId,
			@RequestParam(value = "thirdId", required = false) String thirdId,
			@RequestParam(value = "userId", required = false) Integer userId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return couponService.listCouponByGoodsId(centerId, goodsId,firstId,secondId,thirdId, userId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取指定节点优惠券
	 * @param version
	 * @param List
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/listCouponByNode/{centerId}", method = RequestMethod.GET)
	public ResultModel listCouponByNode(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestParam(value = "node") Integer node) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return couponService.listCouponByNode(node, centerId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 根据优惠券IDS获取优惠券
	 * @param version
	 * @param List
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/listCouponByCouponIds/{centerId}", method = RequestMethod.GET)
	public ResultModel listCouponByCouponIds(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestParam(value = "couponIds") String couponIds,
			@RequestParam(value = "userId") Integer userId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return couponService.listCouponByCouponIds(couponIds, centerId, userId);
		}

		return new ResultModel(false, "");
	}

	@RequestMapping(value = "{version}/update/{centerId}", method = RequestMethod.GET)
	public void updateUserCoupon(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestParam("userId") Integer userId, @RequestParam(value = "couponIds") String couponIds) {
		if (Constants.FIRST_VERSION.equals(version)) {

			couponService.updateUserCoupon(couponIds, centerId, userId);
		}
	}

	@RequestMapping(value = "{version}/updateCouponStatus", method = RequestMethod.POST)
	public void updateCouponStatus(@PathVariable("version") Double version, @RequestBody List<Integer> centerIdList) {
		if (Constants.FIRST_VERSION.equals(version)) {

			couponService.updateCouponStatus(centerIdList);
		}
	}
}
