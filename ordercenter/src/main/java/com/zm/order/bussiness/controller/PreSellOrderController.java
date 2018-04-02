package com.zm.order.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.order.bussiness.service.PreSellOrderService;
import com.zm.order.common.Pagination;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.OrderInfo;

@RestController
public class PreSellOrderController {
	
	@Resource
	PreSellOrderService preSellOrderService;

	@RequestMapping(value = "{version}/order/presell/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity){
		
		if (Constants.FIRST_VERSION.equals(version)) {
			Page<OrderInfo> page = preSellOrderService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
	
	@RequestMapping(value = "{version}/order/presell/pass", method = RequestMethod.POST)
	public ResultModel passPreSellOrder(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody List<String> orderIdList){
		
		if (Constants.FIRST_VERSION.equals(version)) {
			return new ResultModel(true, preSellOrderService.passPreSellOrder(orderIdList));
		}

		return new ResultModel(false, "版本错误");
	}
	
}
