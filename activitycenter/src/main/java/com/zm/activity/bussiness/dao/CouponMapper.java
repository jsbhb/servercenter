package com.zm.activity.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.activity.pojo.Coupon;
import com.zm.activity.pojo.Rule;

public interface CouponMapper {

	List<Coupon> listCoupon(String activityId);

	List<Coupon> listCouponByCouponIds(List<String> couponIdList);
	
	Integer getIssueNumByCouponId(String couponId);
	
	List<String> listUserCouponByUserId(Map<String, Object> param);
	
	List<Rule> listRuleByRuleIds(Map<String, Object> param);

	void updateCouponReceiveNum(Map<String, Object> param);

	int binding(Map<String, Object> param);

	List<Coupon> listIssueNum(List<String> list);

	void updateCouponGiveOut(List<String> list);

	List<Coupon> listCouponSpecialByGoodsId(String goodsId);
	
	List<Coupon> listCouponByCategory(Map<String, Object> param);
	
	List<Coupon> listCouponAllRange();

	List<Coupon> listCouponByNode(Integer node);

	Integer countUserCoupon(Map<String, Object> param);

	void updateUserCoupon(Map<String, Object> param);

	void updateCouponStatus();

}
