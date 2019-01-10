package com.zm.supplier.bussiness.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zm.supplier.bussiness.service.SupplierCallbackService;
import com.zm.supplier.common.ResultModel;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.ErrorCodeEnum;
import com.zm.supplier.pojo.callback.OrderStatusCallBack;

@RestController
public class SupplierCallbackController {
	
	@Resource
	SupplierCallbackService supplierCallbackService;

	@RequestMapping(value = "auth/{version}/order_status")
	public ResultModel orderStatusCallBack(@PathVariable("version") Double version,HttpServletRequest req,
			@RequestBody OrderStatusCallBack statusCallBack) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return supplierCallbackService.orderStatusCallBack(statusCallBack);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}
}
