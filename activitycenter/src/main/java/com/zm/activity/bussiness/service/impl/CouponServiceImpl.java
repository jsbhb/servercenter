package com.zm.activity.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.activity.bussiness.dao.CouponMapper;
import com.zm.activity.bussiness.service.CouponService;
import com.zm.activity.constants.Constants;
import com.zm.activity.pojo.Coupon;
import com.zm.activity.pojo.ResultModel;

@Service
public class CouponServiceImpl implements CouponService {

	@Resource
	CouponMapper couponMapper;

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public List<Coupon> listCoupon(Integer centerId, Integer userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("userId", userId);
		List<String> couponIdList = couponMapper.listUserCouponByUserId(param);
		List<Coupon> couponList = couponMapper.listCoupon(centerId);
		for(Coupon coupon : couponList){
			for(String couponId : couponIdList){
				if(couponId.equals(coupon.getCouponId())){
					coupon.setUserStatus(1);
				}
			}
		}
		return couponList;
	}

	@Override
	public List<Coupon> listCouponByCouponIds(Integer centerId, Integer userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("userId", userId);
		List<String> couponIdList = couponMapper.listUserCouponByUserId(param);
		if (couponIdList == null || couponIdList.size() == 0) {
			return null;
		}
		param.put("list", couponIdList);
		return couponMapper.listCouponByCouponIds(param);
	}

	@Override
	public void createTable(Integer centerId) {
		couponMapper.createCoupon(centerId);
		couponMapper.createCouponGoods(centerId);
		couponMapper.createRule(centerId);
	}

	@Override
	public ResultModel receiveCoupon(Integer centerId, Integer userId, String couponId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("couponId", couponId);
		Integer total = couponMapper.getNumByCouponId(param);
		//如果有数量限制
		if (total != null && total > 0) {
			Object obj = redisTemplate.opsForList().leftPop(Constants.ISSUE_NUM + couponId);
			if (obj == null) {
				return new ResultModel(false, "已抢完");
			}
		}
		param.put("userId", userId);
		int back = couponMapper.binding(param);
		if(back == 1){
			redisTemplate.opsForValue().increment(Constants.RECEIVE_NUM + couponId, 1);
		} else {
			//如果没插入，队列数量返回
			if (total != null && total > 0){
				redisTemplate.opsForList().rightPush(Constants.ISSUE_NUM + couponId,couponId);
			}
		}
		return new ResultModel(true, "领取成功");
	}

}
