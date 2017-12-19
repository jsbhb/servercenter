package com.zm.activity.bussiness.service;

import java.util.List;

import com.zm.activity.pojo.Coupon;
import com.zm.activity.pojo.ResultModel;

public interface CouponService {

	/**
	 * @fun 获取优惠券列表(有userid时区分哪些已领取)
	 * @param centerId
	 * @param userId
	 * @return
	 */
	List<Coupon> listCoupon(Integer centerId, Integer userId, String activityId);

	/**
	 * @fun 根据userId获取用户优惠券
	 * @param centerId
	 * @param userId
	 * @return
	 */
	List<Coupon> listCouponByUserId(Integer centerId, Integer userId, Integer status);

	/**
	 * @fun 新建区域中心时新建数据表
	 * @param centerId
	 * @return
	 */
	void createTable(Integer centerId);

	/**
	 * @fun 用户领取优惠券
	 * @param centerId
	 * @param userId
	 * @param couponId
	 * @return
	 */
	ResultModel receiveCoupon(Integer centerId, Integer userId, String couponId);

	/**
	 * @fun 发放优惠券（可批量）
	 * @param centerId
	 * @param list
	 * @return
	 */
	ResultModel giveOutCoupon(Integer centerId, List<String> list);

	
	ResultModel listCouponByGoodsId(Integer centerId, String goodsId, String userId);
}
