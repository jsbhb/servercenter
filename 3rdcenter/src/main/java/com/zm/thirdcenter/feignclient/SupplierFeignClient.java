package com.zm.thirdcenter.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("suppliercenter")
public interface SupplierFeignClient {

	@RequestMapping(value = "supplier/zs/sign", method = RequestMethod.POST)
	public String getSign(@RequestBody String originData);
}
