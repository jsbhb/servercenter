package com.zm.goods.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.goods.pojo.OrderBussinessModel;

@FeignClient("suppliercenter")
public interface SupplierFeignClient {

	@RequestMapping(value = "{version}/supplier/checkStock/{supplierId}", method = RequestMethod.POST)
	public void checkStock(@PathVariable("version") Double version, @PathVariable("supplierId") Integer supplierId,
			@RequestBody List<OrderBussinessModel> list);
}
