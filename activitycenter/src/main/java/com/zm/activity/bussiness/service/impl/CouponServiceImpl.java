package com.zm.activity.bussiness.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.activity.bussiness.dao.CouponMapper;
import com.zm.activity.bussiness.service.CouponService;
import com.zm.activity.constants.Constants;
import com.zm.activity.pojo.Coupon;
import com.zm.activity.pojo.ResultModel;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

	@Resource
	CouponMapper couponMapper;

	@Resource
	RedisTemplate<String, Object> template;

	private final Integer ALREADY_RECEIVE = 1;
	private final Integer ALREADY_EMPTY = 99;

	@Override
	public List<Coupon> listCoupon(Integer centerId, Integer userId, String activityId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("activityId", activityId);
		List<Coupon> couponList = couponMapper.listCoupon(param);
		if (userId == null) {
			return couponList;
		}
		param.put("userId", userId);
		List<String> couponIdList = couponMapper.listUserCouponByUserId(param);
		for (Coupon coupon : couponList) {
			if (coupon.getNum() > 0) {
				Object obj = template.opsForValue().get(Constants.RECEIVE_NUM + coupon.getCouponId());
				if (obj != null) {
					Integer receiveNum = Integer.valueOf(obj.toString());
					if (receiveNum >= coupon.getNum()) {
						coupon.setStatus(ALREADY_EMPTY);
					}
				}
			}
			for (String couponId : couponIdList) {
				if (couponId.equals(coupon.getCouponId())) {
					coupon.setReceiveStatus(ALREADY_RECEIVE);
				}
			}
		}

		return couponList;
	}

	@Override
	public List<Coupon> listCouponByUserId(Integer centerId, Integer userId, Integer status) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("userId", userId);
		param.put("status", status);
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
		Integer total = couponMapper.getIssueNumByCouponId(param);
		if (total == null) {
			return new ResultModel(false, "没有该优惠券");
		}
		// 如果有数量限制
		if (total > 0) {
			Object obj = template.opsForList().leftPop(Constants.ISSUE_NUM + couponId);
			if (obj == null) {
				return new ResultModel(false, "已抢完");
			}
		}
		param.put("userId", userId);
		int back = couponMapper.binding(param);
		if (back == 1) {
			template.opsForValue().increment(Constants.RECEIVE_NUM + couponId, 1);
			return new ResultModel(true, "领取成功");
		} else {
			// 如果没插入，队列数量返回
			if (total != null && total > 0) {
				template.opsForList().rightPush(Constants.ISSUE_NUM + couponId, couponId);
			}
			return new ResultModel(false, "您已经领取过该优惠券");
		}
	}

	@Override
	public ResultModel giveOutCoupon(Integer centerId, List<String> list) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("centerId", centerId);
			param.put("list", list);
			List<Coupon> couponList = couponMapper.listIssueNum(param);
			if (couponList != null) {
				for (Coupon coupon : couponList) {
					Integer num = coupon.getNum();
					String couponId = coupon.getCouponId();
					for (int i = 0; i < num; i++) {
						template.opsForList().rightPush(Constants.ISSUE_NUM + couponId, couponId);
					}
				}
			}
			couponMapper.updateCouponGiveOut(param);
			return new ResultModel(true, "");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, e.getMessage());
		}

	}

	@Override
	public ResultModel listCouponByGoodsId(Integer centerId, String goodsId, String firstId, String secondId,
			String thirdId, Integer userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("goodsId", goodsId);
		param.put("firstId", firstId);
		param.put("secondId", secondId);
		param.put("thirdId", thirdId);
		List<Coupon> couponList = couponMapper.listCouponSpecialByGoodsId(param);//range=4
		couponList.addAll(couponMapper.listCouponByCategory(param));//range = 1,2,3
		couponList.addAll(couponMapper.listCouponAllRange(param));//range = 0
		List<String> couponIdList = null;
		if (userId != null) {
			param.put("userId", userId);
			couponIdList = couponMapper.listUserCouponByUserId(param);
		}
		for (Coupon coupon : couponList) {
			if (couponIdList != null) {
				for (String couponId : couponIdList) {
					if (couponId.equals(coupon.getCouponId())) {
						coupon.setReceiveStatus(ALREADY_RECEIVE);
					}
				}
			}
			if (coupon.getNum() > 0) {
				Object obj = template.opsForValue().get(Constants.RECEIVE_NUM + coupon.getCouponId());
				if (obj != null) {
					Integer receiveNum = Integer.valueOf(obj.toString());
					if (receiveNum >= coupon.getNum()) {
						coupon.setStatus(ALREADY_EMPTY);
					}
				}
			}
		}

		return new ResultModel(true, couponList);
	}

	@Override
	public ResultModel listCouponByNode(Integer node, Integer centerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("node", node);

		return new ResultModel(true, couponMapper.listCouponByNode(param));
	}

	@Override
	public ResultModel listCouponByCouponIds(String couponIds, Integer centerId, Integer userId) {
		String[] couponIdArr = couponIds.split(",");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("userId", userId);
		param.put("list", Arrays.asList(couponIdArr));
		Integer count = couponMapper.countUserCoupon(param);
		if (count != couponIdArr.length) {
			return new ResultModel(false, "优惠券使用非法");
		}
		return new ResultModel(true, couponMapper.listCouponByCouponIds(param));
	}

	@Override
	public void updateUserCoupon(String couponIds, Integer centerId, Integer userId) {
		String[] couponIdArr = couponIds.split(",");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("centerId", centerId);
		param.put("list", Arrays.asList(couponIdArr));
		param.put("userId", userId);
		couponMapper.updateUserCoupon(param);

	}

	@Override
	public void updateCouponStatus(List<Integer> centerIdList) {
		for (Integer centerId : centerIdList) {
			couponMapper.updateCouponStatus(centerId);
		}
	}

}
