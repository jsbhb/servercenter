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
import com.zm.order.bussiness.service.OrderStockOutService;
import com.zm.order.common.Pagination;
import com.zm.order.common.ResultModel;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.ThirdOrderInfo;

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
public class OrderStockOutController {

	@Resource
	OrderStockOutService orderStockOutService;

	@RequestMapping(value = "{version}/order/stockOut/queryForPage", method = RequestMethod.POST)
	public ResultModel queryForPage(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Page<OrderInfo> page = orderStockOutService.queryByPage(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/order/stockOut/queryForPageForGoods", method = RequestMethod.POST)
	public ResultModel queryForPageForGoods(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderGoods entity) {

		if (Constants.FIRST_VERSION.equals(version)) {

			entity.setOrderId(request.getParameter("orderId"));

			Page<OrderGoods> page = orderStockOutService.queryByPageForGoods(entity);
			return new ResultModel(true, page, new Pagination(page));
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/order/stockOut/query", method = RequestMethod.POST)
	public ResultModel queryById(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getOrderId() == null || "".equals(entity.getOrderId())) {
					return new ResultModel(false, "没有编号信息");
				}

				OrderInfo result = orderStockOutService.queryByOrderId(entity.getOrderId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}

	@RequestMapping(value = "{version}/order/stockOut/queryThirdInfo", method = RequestMethod.POST)
	public ResultModel queryThirdInfo(HttpServletRequest request, @PathVariable("version") Double version,
			@RequestBody OrderInfo entity) {

		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				if (entity.getOrderId() == null || "".equals(entity.getOrderId())) {
					return new ResultModel(false, "没有编号信息");
				}

				List<ThirdOrderInfo> result = orderStockOutService.queryThirdInfo(entity.getOrderId());
				return new ResultModel(true, result);
			}

			return new ResultModel(false, "版本错误");
		} catch (Exception e) {
			return new ResultModel(false, e.getMessage());
		}
	}
}
