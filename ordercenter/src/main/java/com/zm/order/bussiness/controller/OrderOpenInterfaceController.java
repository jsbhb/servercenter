package com.zm.order.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderOpenInterfaceService;
import com.zm.order.constants.Constants;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.ResultModel;

@RestController
public class OrderOpenInterfaceController {

	@Resource
	OrderOpenInterfaceService orderOpenInterfaceService;

	@RequestMapping(value = "{version}/add_order", method = RequestMethod.POST)
	public ResultModel addThirdOrder(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String order = req.getParameter("data");
			String appKey = req.getParameter("appKey");
			if (order == null || appKey == null || "".equals(order) || "".equals(appKey)) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
			try {
				return orderOpenInterfaceService.addOrder(order);
			} catch (Exception e) {
				LogUtil.writeErrorLog("【对接订单出错】",e);
				if (e.getMessage().contains("Duplicate entry")) {
					return new ResultModel(false, ErrorCodeEnum.REPEAT_ERROR.getErrorCode(),
							ErrorCodeEnum.REPEAT_ERROR.getErrorMsg());
				}
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "{version}/order_status", method = RequestMethod.POST)
	public ResultModel getOrderStatus(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			String appKey = req.getParameter("appKey");
			if (data == null || appKey == null || "".equals(data) || "".equals(appKey)) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorCode());
			}
			try {
				return orderOpenInterfaceService.getOrderStatus(data);
			} catch (Exception e) {
				LogUtil.writeErrorLog("【对接获取订单状态出错】",e);
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}

		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
	
	@RequestMapping(value = "{version}/pay_custom", method = RequestMethod.POST)
	public ResultModel payCustom(@PathVariable("version") Double version, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String data = req.getParameter("data");
			String appKey = req.getParameter("appKey");
			if (data == null || appKey == null || "".equals(data) || "".equals(appKey)) {
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorCode());
			}
			try {
				return orderOpenInterfaceService.payCustom(data);
			} catch (Exception e) {
				LogUtil.writeErrorLog("【对接订单支付单报关出错】",e);
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}

		}

		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
