package com.zm.goods.bussiness.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.goods.constants.Constants;
import com.zm.goods.feignclient.ActivityFeignClient;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.vo.Coupon;

@Component
public class ActivityComponent {

	@Resource
	ActivityFeignClient activityFeignClient;

	@SuppressWarnings("unchecked")
	public void doPackCoupon(Integer centerId, Integer userId, List<GoodsItem> goodsList) {
		ResultModel result = activityFeignClient.listCouponByGoodsId(Constants.FIRST_VERSION, centerId,
				goodsList.get(0).getGoodsId(), goodsList.get(0).getFirstCategory(),
				goodsList.get(0).getSecondCategory(), goodsList.get(0).getThirdCategory(), userId);
		if (result.isSuccess()) {
			List<Coupon> couponList = (List<Coupon>) result.getObj();
			goodsList.get(0).setCouponList(couponList);
		}
	}

	public ResultModel listCouponByCouponIds(Integer centerId, String couponIds, Integer userId) {

		return activityFeignClient.listCouponByCouponIds(Constants.FIRST_VERSION, centerId, couponIds, userId);
	}
}
