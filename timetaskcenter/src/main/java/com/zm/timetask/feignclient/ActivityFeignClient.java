package com.zm.timetask.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.timetask.pojo.ResultModel;

@FeignClient("activitycenter")
public interface ActivityFeignClient {

	@RequestMapping(value = "{version}/giveout-coupon/{centerId}", method = RequestMethod.POST)
	public ResultModel giveOutCoupon(@PathVariable("version") Double version,
			@PathVariable("centerId") Integer centerId, @RequestBody List<String> list);
	
	@RequestMapping(value = "{version}/updateCouponStatus", method = RequestMethod.POST)
	public void updateCouponStatus(@PathVariable("version") Double version, @RequestBody List<Integer> centerIdList);
}
