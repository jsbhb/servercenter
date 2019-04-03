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
import com.zm.supplier.log.LogUtil;
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
		String type = req.getParameter("type");
		String time = req.getParameter("time");
		String name = req.getParameter("name");
		String md5 = req.getParameter("md5");
		String debug = req.getParameter("debug");
		String data = req.getParameter("data");
		getParam.put("type", type);
		getParam.put("time", time);
		getParam.put("name", name);
		getParam.put("md5", md5);
		getParam.put("debug", debug);
		Map<String, String> postParam = new HashMap<String, String>();
		postParam.put("data", data);
		LogUtil.writeLog("type=" + type + ";time=" + time + ";name=" + name + ";md5=" + md5 + ";debug=" + debug
				+ ";data=" + data);
		return supplierCallbackService.dolphinCallBack(getParam, postParam);
	}

	@RequestMapping(value = "auth/hz-customs/callback", method = RequestMethod.POST)
	public String hzCustomsCallback(@RequestParam("content") String content, @RequestParam("msg_type") String msgType,
			@RequestParam("data_digest") String dataDigest) {

		return supplierCallbackService.hzCustomsCallback(content, msgType, dataDigest);
	}

}
