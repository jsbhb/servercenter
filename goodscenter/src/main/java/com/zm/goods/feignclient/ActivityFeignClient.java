package com.zm.goods.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.goods.pojo.ResultModel;

@FeignClient("activitycenter")
public interface ActivityFeignClient {

	@RequestMapping(value = "{version}/coupon-goodsId/{centerId}", method = RequestMethod.GET)
	public ResultModel listCouponByGoodsId(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestParam("goodsId") String goodsId,
			@RequestParam(value = "userId", required = false) String userId);

	@RequestMapping(value = "{version}/listCouponByCouponIds/{centerId}", method = RequestMethod.GET)
	public ResultModel listCouponByCouponIds(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestParam(value = "couponIds") String couponIds,
			@RequestParam(value = "userId") Integer userId);
}
