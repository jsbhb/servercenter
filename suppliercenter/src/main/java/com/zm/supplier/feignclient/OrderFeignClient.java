package com.zm.supplier.feignclient;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdOrderInfo;
import com.zm.supplier.pojo.callback.OrderStatusCallBack;

@FeignClient("ordercenter")
public interface OrderFeignClient {

	@RequestMapping(value = "{version}/order/saveThirdOrder", method = RequestMethod.POST)
	public boolean saveThirdOrder(@PathVariable("version") Double version, @RequestBody Set<SendOrderResult> set);

	@RequestMapping(value = "{version}/order/changeOrderStatusByThirdWarehouse", method = RequestMethod.POST)
	public boolean changeOrderStatusByThirdWarehouse(@PathVariable("version") Double version,
			@RequestBody List<ThirdOrderInfo> list);

	@RequestMapping(value = "{version}/order/status/call-back", method = RequestMethod.POST)
	public ResultModel orderStatusCallBack(@PathVariable("version") Double version,
			@RequestBody OrderStatusCallBack callBack);
}
