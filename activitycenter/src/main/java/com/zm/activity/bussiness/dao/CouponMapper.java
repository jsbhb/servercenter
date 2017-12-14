package com.zm.activity.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.activity.pojo.Coupon;

public interface CouponMapper {

	List<Coupon> listCoupon(Integer centerId);

	List<Coupon> listCouponByCouponIds(Map<String, Object> param);
	
	void createCoupon(@Param("centerId")Integer centerId);
	
	void createRule(@Param("centerId")Integer centerId);
	
	void createCouponGoods(@Param("centerId")Integer centerId);

	Integer getNumByCouponId(Map<String, Object> param);
	
	List<String> listUserCouponByUserId(Map<String, Object> param);

	void updateCouponReceiveNum(Map<String, Object> param);

	int binding(Map<String, Object> param);

}
