package com.zm.order.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("activitycenter")
public interface ActivityFeignClient {

	@RequestMapping(value = "{version}/update/{centerId}", method = RequestMethod.GET)
	public void updateUserCoupon(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@RequestParam("userId") Integer userId, @RequestParam(value = "couponIds") String couponIds);

}
