package com.zm.order.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.ResultModel;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list, boolean delStock, boolean vip);

	@RequestMapping(value = "{version}/goods/goodsSpecs", method = RequestMethod.POST)
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, @RequestBody List<String> list);
}
