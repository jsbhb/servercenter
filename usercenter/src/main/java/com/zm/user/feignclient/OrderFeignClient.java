package com.zm.user.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.user.common.ResultModel;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	@RequestMapping(value = "{version}/order/table/{centerId}", method = RequestMethod.POST)
	public ResultModel createTable(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId);

	@RequestMapping(value = "{version}/order/repayingPushJudge/{shopId}/{pushUserId}", method = RequestMethod.GET)
	public ResultModel repayingPushJudge(@PathVariable("version") Double version,
			@PathVariable("shopId") Integer shopId, @PathVariable("pushUserId") Integer pushUserId);

	@RequestMapping(value = "{version}/order/pushUserOrderCount/{shopId}", method = RequestMethod.POST)
	public ResultModel pushUserOrderCount(@PathVariable("version") Double version,
			@PathVariable("shopId") Integer shopId, @RequestBody List<Integer> pushUserIdList);
}
