package com.zm.pay.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.pay.feignclient.model.UserVip;
import com.zm.pay.pojo.ResultModel;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "{version}/user/getVipUser/{orderId}", method = RequestMethod.GET)
	public UserVip getClientIdByOrderId(@PathVariable("orderId") String orderId,
			@PathVariable("version") Double version);

	@RequestMapping(value = "{version}/user/updateUservip", method = RequestMethod.POST)
	public ResultModel updateUserVip(@PathVariable("version") Double version, @RequestBody UserVip userVip);

	@RequestMapping(value = "{version}/user/uservip", method = RequestMethod.POST)
	public ResultModel saveUserVip(@PathVariable("version") Double version, @RequestBody UserVip userVip);

	@RequestMapping(value = "{version}/user/vip/{centerId}/{userId}", method = RequestMethod.GET)
	public boolean getVipUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId);

	@RequestMapping(value = "{version}/user/updateVipOrder/{orderId}", method = RequestMethod.PUT)
	public boolean updateVipOrder(@PathVariable("version") Double version, @PathVariable("orderId") String orderId);
}
