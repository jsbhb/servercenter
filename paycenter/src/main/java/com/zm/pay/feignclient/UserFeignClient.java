package com.zm.pay.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.pay.feignclient.model.UserInfo;
import com.zm.pay.feignclient.model.UserVip;
import com.zm.pay.pojo.ResultModel;

@FeignClient("usercenter")
public interface UserFeignClient {

	/**
	 * @fun 根据订单号获取会员用户（包括centerID）
	 * @param orderId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "{version}/user/getVipUser/{orderId}", method = RequestMethod.GET)
	public UserVip getClientIdByOrderId(@PathVariable("orderId") String orderId,
			@PathVariable("version") Double version);

	/**
	 * @fun 更新会员信息
	 * @param version
	 * @param userVip
	 * @return
	 */
	@RequestMapping(value = "{version}/user/updateUservip", method = RequestMethod.POST)
	public ResultModel updateUserVip(@PathVariable("version") Double version, @RequestBody UserVip userVip);

	/**
	 * @fun 保存VIP信息
	 * @param version
	 * @param userVip
	 * @return
	 */
	@RequestMapping(value = "{version}/user/uservip", method = RequestMethod.POST)
	public ResultModel saveUserVip(@PathVariable("version") Double version, @RequestBody UserVip userVip);

	/**
	 * @fun 判断用户是否是VIP
	 * @param version
	 * @param userId
	 * @param centerId
	 * @return
	 */
	@RequestMapping(value = "{version}/user/vip/{centerId}/{userId}", method = RequestMethod.GET)
	public UserInfo getVipUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId);

	/**
	 * @fun 支付成功后更新VIP订单
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/user/updateVipOrder/{orderId}", method = RequestMethod.PUT)
	public boolean updateVipOrder(@PathVariable("version") Double version, @PathVariable("orderId") String orderId);
	
	/**
	 * @fun 微信多次回调时判断是否已经处理（幂等）
	 * @param version
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "{version}/user/is-already-pay/{orderId}", method = RequestMethod.GET)
	public boolean isAlreadyPay(@PathVariable("version") Double version, @PathVariable("orderId") String orderId);

	@RequestMapping(value = "{version}/grade-url/{centerId}", method = RequestMethod.GET)
	public String getClientUrl(@PathVariable("centerId")Integer centerId, @PathVariable("version")Double version);
}
