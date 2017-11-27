package com.zm.order.feignclient;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.ResultModel;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version,
			@RequestBody List<OrderBussinessModel> list, @RequestParam("supplierId") Integer supplierId,
			@RequestParam("vip") boolean vip, @RequestParam("centerId") Integer centerId,
			@RequestParam("orderFlag") Integer orderFlag);

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs", method = RequestMethod.GET)
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, @RequestParam("itemIds") String ids,
			@RequestParam("centerId") Integer centerId);

	@RequestMapping(value = "auth/{version}/goods/active", method = RequestMethod.GET)
	public ResultModel getActivity(@PathVariable("version") Double version, @RequestParam("type") Integer type,
			@RequestParam("typeStatus") Integer typeStatus, @RequestParam("centerId") Integer centerId);

	@RequestMapping(value = "{version}/goods/stockback", method = RequestMethod.POST)
	public ResultModel stockBack(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			@RequestParam("orderFlag") Integer orderFlag);
}
