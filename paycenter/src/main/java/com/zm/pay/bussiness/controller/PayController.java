package com.zm.pay.bussiness.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.pay.constants.Constants;
import com.zm.pay.constants.LogConstants;
import com.zm.pay.feignclient.LogFeignClient;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.utils.CommonUtils;
import com.zm.pay.utils.wx.WxPayUtils;

@RestController
public class PayController {

	@Resource
	RedisTemplate<String, ?> redisTemplate;

	@Resource
	LogFeignClient logFeignClient;

	@RequestMapping(value = "wxpay/{type}/{clientId}", method = RequestMethod.POST)
	public Map<String, String> wxPay(@PathVariable("clientId") Integer clientId, @PathVariable("type") String type,
			@RequestBody PayModel model) throws Exception {

		WeixinPayConfig config = (WeixinPayConfig) redisTemplate.opsForValue()
				.get(Constants.PAY + clientId + Constants.WX_PAY);
		
		config.setHttpConnectTimeoutMs(5000);
		config.setHttpReadTimeoutMs(5000);

		Map<String, String> result = WxPayUtils.unifiedOrder(type, config, model);

		String return_code = (String) result.get("return_code");

		if (return_code.equals("SUCCESS")) {
			String content = "订单号 \"" + model.getOrderId() + "\" 通过微信支付，后台请求成功";
			logFeignClient.saveLog(Constants.FIRST_VERSION,
					CommonUtils.packageLog(LogConstants.WX_PAY, "微信支付", clientId, content, ""));
		}

		return result;
	}

}
