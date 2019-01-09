package com.zm.order.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.bussiness.service.PurchaseOrderService;
import com.zm.order.common.Pagination;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.OrderInfo;

/**
 * ClassName: OrderBackController <br/>
 * Function: 订单后台API控制器. <br/>
 * date: Aug 22, 2017 9:51:44 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class PurchaseOrderController {

	@Resource
	PurchaseOrderService purchaseOrderService;
	
	@Resource
	OrderStockOutService orderStockOutService;

//	@RequestMapping(value = "{version}/order/purchase/queryForPage", method = RequestMethod.POST)
//	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
//			@RequestBody PurchaseOrderInfo entity) {
//
//		if (Constants.FIRST_VERSION.equals(version)) {
//			Page<PurchaseOrderInfo> page = purchaseOrderService.queryByPage(entity);
//			return new ResultModel(true, page, new Pagination(page));
//		}
//
//		return new ResultModel(false, "版本错误");
//	}
	
	@RequestMapping(value = "{version}/order/purchase/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<OrderInfo> page = purchaseOrderService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}
}
