package com.zm.pay.utils.wx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.CustomConfig;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.utils.CommonUtils;

public class WxPayUtils {

	private static final String CUSTOM_URL = "https://api.mch.weixin.qq.com/cgi-bin/mch/customs/customdeclareorder";

	private static final Integer CONNECT_TIMEOUTMS = 5000;
	private static final Integer READ_TIMEOUTMS = 5000;

	private static Logger logger = LoggerFactory.getLogger(WxPayUtils.class);

	// 微信退款接口
	public static Map<String, String> wxRefundPay(WeixinPayConfig config, RefundPayModel model) throws Exception {
		WXPay wxpay = new WXPay(config);

		Map<String, String> data = new HashMap<String, String>();
		data.put("transaction_id", model.getPayNo());
		data.put("out_trade_no", model.getOrderId());
		data.put("out_refund_no", model.getOrderId());
		data.put("fee_type", Constants.FEE_TYPE);
		data.put("total_fee", Double.valueOf(model.getAmount()) * 100 + "");
		data.put("refund_fee", Double.valueOf(model.getAmount()) * 100 + "");
		data.put("refund_fee_type", "CNY");
		data.put("refund_desc", model.getReason());

		Map<String, String> resp = wxpay.refund(data);
		logger.info(resp.toString());
		return resp;
	}

	// 报关接口
	public static Map<String, String> acquireCustom(WeixinPayConfig config, CustomModel custom, CustomConfig customCfg)
			throws Exception {

		WXPay wxpay = new WXPay(config);

		Map<String, String> data = new HashMap<String, String>();
		data.put("out_trade_no", custom.getOutRequestNo());
		data.put("transaction_id", custom.getPayNo());
		data.put("customs", customCfg.getWxCustomsPlace());
		data.put("mch_customs_no", customCfg.getMerchantCustomsCode());

		data = fillRequestData(data, config);
		String res = wxpay.requestWithoutCert(CUSTOM_URL, data, CONNECT_TIMEOUTMS, READ_TIMEOUTMS);
		logger.info(res);

		return CommonUtils.xmlToMap(res);
	}

	// 分装微信支付返回结果
	public static void packageReturnParameter(Integer clientId, String type, PayModel model, WeixinPayConfig config,
			Map<String, String> resp, Map<String, String> result) throws Exception {

		// 封装支付信息给前台
		if (Constants.NATIVE.equals(type)) {
			String urlCode = (String) resp.get("code_url");
			result.put("urlCode", urlCode);
		} else if (Constants.JSAPI.equals(type) || Constants.JSAPI_WX_APPLET.equalsIgnoreCase(type)) {
			result.put("timeStamp", System.currentTimeMillis() / 1000 + "");
			result.put("package", "prepay_id=" + resp.get("prepay_id"));
			result.put("appId", config.getAppID());
			result.put("nonceStr", resp.get("nonce_str"));
			result.put("signType", "MD5");
			String sign = WXPayUtil.generateSignature(result, config.getKey());
			result.put("paySign", sign);
		}
		result.put("success", "true");
	}

	public static Map<String, String> fillRequestData(Map<String, String> data, WeixinPayConfig config) {
		data.put("appid", config.getAppID());
		data.put("mch_id", config.getMchID());
		Set<String> keySet = data.keySet();
		String keyArray[] = (String[]) keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		String as[] = keyArray;
		int i = as.length;
		for (int j = 0; j < i; j++) {
			String k = as[j];
			if (!k.equals("sign") && ((String) data.get(k)).trim().length() > 0)
				sb.append(k).append("=").append(((String) data.get(k)).trim()).append("&");
		}

		sb.append("key=").append(config.getKey());
		data.put("sign", DigestUtils.md5Hex(sb.toString()).toUpperCase());

		return data;
	}

	public static void main(String[] args) throws Exception {
		WeixinPayConfig config = new WeixinPayConfig();
		WXPay wxpay = new WXPay(config);
		config.setAppID("wxc05991a451e0fefe");
		config.setMchID("1306018601");
		config.setKey("r4wNJZxtwaj5ai6FHOeMEXFrxj8R8tpP");
		Map<String, String> data = new HashMap<String, String>();
		data.put("customs", Constants.CUSTOMS_PLACE);
		data.put("mch_customs_no", Constants.MERCHANT_CUSTOMS_CODE);
		data.put("out_trade_no", "GX0171117104436810016");
		data.put("transaction_id", "4200000003201711175187295278");
		data = fillRequestData(data, config);
		System.out.println(data);
		String res = wxpay.requestWithoutCert(CUSTOM_URL, data, CONNECT_TIMEOUTMS, READ_TIMEOUTMS);
		System.out.println(res);
	}

}
