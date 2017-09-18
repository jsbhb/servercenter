package com.zm.pay.utils.wx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.WXPay;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.WeixinPayConfig;

public class WxPayUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public static Map<String, String> unifiedOrder(String type, WeixinPayConfig config, PayModel model)
			throws Exception {

		WXPay wxpay = new WXPay(config);

		Map<String, String> data = new HashMap<String, String>();
		data.put("body", model.getBody());
		data.put("out_trade_no", model.getOrderId());
		data.put("device_info", "");
		data.put("fee_type", Constants.FEE_TYPE);
		data.put("total_fee", model.getTotalAmount());
		data.put("notify_url", Constants.WX_NOTIFY_URL);
		data.put("detail", model.getDetail() == null ? "" : model.getDetail());
		// 支付时间设定为5分钟
		Calendar cal = Calendar.getInstance();
		data.put("time_start", sdf.format(cal.getTime()));
		cal.add(Calendar.MINUTE, 5);
		data.put("time_expire", sdf.format(cal.getTime()));
		data.put("trade_type", type);
		if (Constants.JSAPI.equals(type)) {
			data.put("openId", model.getOpenId());
		} else if (Constants.MWEB.equals(type) || Constants.APP.equals(type)) {
			data.put("spbill_create_ip", model.getIP());
		} else if (Constants.NATIVE.equals(type)) {
			data.put("spbill_create_ip", Constants.CREATE_IP);
		}

		Map<String, String> resp = wxpay.unifiedOrder(data);
		System.out.println(resp);
		return resp;
	}
}
