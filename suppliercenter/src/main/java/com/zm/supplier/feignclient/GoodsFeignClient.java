package com.zm.supplier.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.supplier.pojo.WarehouseStock;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "/{version}/goods/stock", method = RequestMethod.POST)
	public boolean updateThirdWarehouseStock(@PathVariable("version") Double version, @RequestBody List<WarehouseStock> list);
}
