package com.zm.supplier.feignclient;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.WarehouseStock;
import com.zm.supplier.pojo.bo.CustomCompletion;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "/{version}/goods/stock", method = RequestMethod.POST)
	public boolean updateThirdWarehouseStock(@PathVariable("version") Double version, @RequestBody List<WarehouseStock> list);

	@RequestMapping(value = "/{version}/goods/thirdGoods", method = RequestMethod.POST)
	public boolean saveThirdGoods(@PathVariable("version") Double version, @RequestBody Set<ThirdWarehouseGoods> set);
	
	@RequestMapping(value = "{version}/goods/feign/custom/completion", method = RequestMethod.POST)
	public List<CustomCompletion> customCompletion(@PathVariable("version") Double version,
			@RequestBody List<String> itemIdList);
}
