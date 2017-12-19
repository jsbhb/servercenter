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
	 * @fun 新建区域中心时新建表
	 * @param version
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/createTable/{centerId}", method = RequestMethod.POST)
	public boolean createTable(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			couponService.createTable(centerId);
			return true;
		}

		return false;
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
}
