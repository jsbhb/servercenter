package com.zm.supplier.bussiness.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "auth/{version}/order_status", method = RequestMethod.POST)
	public ResultModel orderStatusCallBack(@PathVariable("version") Double version, HttpServletRequest req,
			@RequestBody OrderStatusCallBack statusCallBack) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return supplierCallbackService.orderStatusCallBack(statusCallBack);
		}
		return new ResultModel(false, ErrorCodeEnum.VERSION_ERROR.getErrorCode(),
				ErrorCodeEnum.VERSION_ERROR.getErrorMsg());
	}

	@RequestMapping(value = "auth/dolphin/callback", method = RequestMethod.POST)
	public String dolphinCallBack(HttpServletRequest req) {
		Map<String, String> getParam = new HashMap<String, String>();
		getParam.put("type", req.getParameter("type"));
		getParam.put("time", req.getParameter("time"));
		getParam.put("name", req.getParameter("name"));
		getParam.put("md5", req.getParameter("md5"));
		getParam.put("debug", req.getParameter("debug"));
		Map<String, String> postParam = new HashMap<String, String>();
		postParam.put("data", req.getParameter("data"));
		return supplierCallbackService.dolphinCallBack(getParam, postParam);
	}

	@RequestMapping(value = "auth/hz-customs/callback", method = RequestMethod.POST)
	public String hzCustomsCallback(@RequestParam("content") String content, @RequestParam("msg_type") String msgType,
			@RequestParam("data_digest") String dataDigest) {

		return supplierCallbackService.hzCustomsCallback(content, msgType, dataDigest);
	}

}
