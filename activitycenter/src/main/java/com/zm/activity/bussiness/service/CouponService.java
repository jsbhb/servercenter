package com.zm.activity.bussiness.service;

import java.util.List;

import com.zm.activity.pojo.Coupon;
import com.zm.activity.pojo.ResultModel;

public interface CouponService {

	List<Coupon> listCoupon(Integer centerId, Integer userId);

	List<Coupon> listCouponByCouponIds(Integer centerId, Integer userId);

	void createTable(Integer centerId);

	ResultModel receiveCoupon(Integer centerId, Integer userId, String couponId);
}
