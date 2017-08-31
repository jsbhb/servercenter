package com.zm.pay.utils.wx;

import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.WXPay;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.WeixinPayConfig;

public class WxPayUtils {

	public static Map<String, String> unifiedOrder(String type, WeixinPayConfig config, PayModel model, String openId)
			throws Exception {

		WXPay wxpay = new WXPay(config);

		Map<String, String> data = new HashMap<String, String>();
		data.put("body", model.getBody());
		data.put("out_trade_no", model.getOrderId());
		data.put("fee_type", Constants.FEE_TYPE);
		data.put("total_fee", model.getTotalAmount());
		data.put("spbill_create_ip", Constants.CREATE_IP);
		data.put("notify_url", Constants.WX_NOTIFY_URL);
		data.put("detail", model.getDetail());
		if(Constants.JSAPI.equals(type)){
			data.put("trade_type", Constants.JSAPI);
			data.put("openId", openId);
		}

		Map<String, String> resp = wxpay.unifiedOrder(data);
		System.out.println(resp);
		return resp;
	}
}
