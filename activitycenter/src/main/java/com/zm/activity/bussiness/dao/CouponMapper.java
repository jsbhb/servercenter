package com.zm.activity.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.activity.pojo.Coupon;
import com.zm.activity.pojo.Rule;

public interface CouponMapper {

	List<Coupon> listCoupon(Map<String, Object> param);

	List<Coupon> listCouponByCouponIds(Map<String, Object> param);
	
	void createCoupon(@Param("centerId")Integer centerId);
	
	void createRule(@Param("centerId")Integer centerId);
	
	void createCouponGoods(@Param("centerId")Integer centerId);

	Integer getIssueNumByCouponId(Map<String, Object> param);
	
	List<String> listUserCouponByUserId(Map<String, Object> param);
	
	List<Rule> listRuleByRuleIds(Map<String, Object> param);

	void updateCouponReceiveNum(Map<String, Object> param);

	int binding(Map<String, Object> param);

	List<Coupon> listIssueNum(Map<String, Object> param);

	void updateCouponGiveOut(Map<String, Object> param);

	List<Coupon> listCouponByGoodsId(Map<String, Object> param);

	List<Coupon> listCouponByNode(Map<String, Object> param);

	Integer countUserCoupon(Map<String, Object> param);

	void updateUserCoupon(Map<String, Object> param);

}
