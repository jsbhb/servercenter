package com.zm.pay.bussiness.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.pay.bussiness.service.PayService;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.pojo.ResultModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "支付服务接口", description = "支付服务接口")
public class PayController {

	@Resource
	PayService payService;

	@RequestMapping(value = "wxpay/{type}/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, String> wxPay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model, HttpServletRequest req) throws Exception {

		return payService.weiXinPay(clientId, type, model);

	}

	@RequestMapping(value = "alipay/{type}/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, Object> aliPay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model, HttpServletRequest req) throws Exception {

		return payService.aliPay(clientId, type, model);

	}

	@RequestMapping(value = "unionpay/{type}/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, Object> unionpay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model) {

		return payService.unionPay(clientId, model, type);

	}

	@RequestMapping(value = "alipay/refund/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, Object> aliRefundPay(@PathVariable("clientId") Integer clientId,
			@RequestBody RefundPayModel model) throws Exception {

		return payService.aliRefundPay(clientId, model);

	}

	@RequestMapping(value = "wx/refund/{clientId}", method = RequestMethod.POST)
	@ApiIgnore
	public Map<String, Object> wxRefundPay(@PathVariable("clientId") Integer clientId,
			@RequestBody RefundPayModel model) throws Exception {

		return payService.wxRefundPay(clientId, model);

	}

	@RequestMapping(value = "{version}/pay/{payType}/{type}/{orderId}", method = RequestMethod.POST)
	@ApiOperation(value = "付款接口", response = ResultModel.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0"),
			@ApiImplicitParam(paramType = "path", name = "payType", dataType = "Integer", required = true, value = "付款方式1微信，2支付宝"),
			@ApiImplicitParam(paramType = "path", name = "type", dataType = "String", required = true, value = "支付模式，如公众号支付JSAPI"),
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单号") })
	public ResultModel pay(@PathVariable("orderId") String orderId, @PathVariable("version") Double version,
			@PathVariable("type") String type, @PathVariable("payType") Integer payType, HttpServletRequest req)
					throws Exception {

		if (Constants.FIRST_VERSION.equals(version)) {

			return payService.pay(version, type, payType, req, orderId);

		}

		return null;
	}

	@RequestMapping(value = "pay/paycustom", method = RequestMethod.POST)
	@ApiIgnore
	public boolean payCustom(@RequestBody CustomModel model) throws Exception {

		return payService.payCustom(model);
	}
	
	@RequestMapping(value = "pay/testHttps", method = RequestMethod.POST)
	@ApiIgnore
	public String testHttps() throws Exception {

		return payService.testHttps();
	}

}
