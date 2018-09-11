package com.zm.order.feignclient;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.order.pojo.bo.RebateDownload;

@FeignClient("financecenter")
public interface FinanceFeignClient {

	@RequestMapping(value = "{version}/finance/rebate/detail/save", method = RequestMethod.POST)
	public void saveRebateDetail(@PathVariable("version") Double version, @RequestBody Map<String, String> map);

	@RequestMapping(value = "{version}/finance/rebate/detail/finsh", method = RequestMethod.POST)
	public void updateRebateDetail(@PathVariable("version") Double version, @RequestParam("orderId") String orderId,
			@RequestParam("status") Integer status);

	@RequestMapping(value = "{version}/finance/rebate/detail/download", method = RequestMethod.POST)
	public List<RebateDownload> listRebateDetailForDownload(@PathVariable("version") Double version,
			@RequestBody Set<String> orderIds);

}
