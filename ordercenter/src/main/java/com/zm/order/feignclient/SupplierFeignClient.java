package com.zm.order.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.pojo.OrderIdAndSupplierId;

@FeignClient("suppliercenter")
public interface SupplierFeignClient {

	@RequestMapping(value = "{version}/supplier/checkOrderStatus", method = RequestMethod.POST)
	public void checkOrderStatus(@PathVariable("version") Double version,
			@RequestBody List<OrderIdAndSupplierId> list);
}
