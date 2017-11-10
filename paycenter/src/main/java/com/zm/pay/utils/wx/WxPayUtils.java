package com.zm.pay.utils.wx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.utils.CommonUtils;

public class WxPayUtils {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final String CUSTOM_URL = "https://api.mch.weixin.qq.com/cgi-bin/mch/customs/customdeclareorder";
	
	private static final Integer CONNECT_TIMEOUTMS = 5000;
	private static final Integer READ_TIMEOUTMS = 5000;
	
	private static Logger logger = LoggerFactory.getLogger(WxPayUtils.class);
	
	//统一下单接口
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
		cal.add(Calendar.HOUR, Constants.PAY_EFFECTIVE_TIME_HOUR);
		data.put("time_expire", sdf.format(cal.getTime()));
		data.put("trade_type", type);
		if (Constants.JSAPI.equals(type)) {
			data.put("openid", model.getOpenId());
			data.put("spbill_create_ip", Constants.CREATE_IP);
		} else if (Constants.MWEB.equals(type) || Constants.APP.equals(type)) {
			data.put("spbill_create_ip", model.getIP());
		} else if (Constants.NATIVE.equals(type)) {
			data.put("spbill_create_ip", Constants.CREATE_IP);
		}

		Map<String, String> resp = wxpay.unifiedOrder(data);
		logger.info(resp.toString());
		return resp;
	}
	
	
	//微信退款接口
	public static Map<String, String> wxRefundPay(WeixinPayConfig config, RefundPayModel model) throws Exception{
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
	
	//报关接口
	public static Map<String,String> acquireCustom(WeixinPayConfig config, CustomModel custom) throws Exception{
		
		WXPay wxpay = new WXPay(config);
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("out_trade_no", custom.getOutRequestNo());
		data.put("transaction_id", custom.getPayNo());
		data.put("customs", Constants.CUSTOMS_PLACE);
		data.put("mch_customs_no", Constants.MERCHANT_CUSTOMS_CODE);
		
		data = wxpay.fillRequestData(data);
		String res = wxpay.requestWithoutCert(CUSTOM_URL, data, CONNECT_TIMEOUTMS, READ_TIMEOUTMS);
		logger.info(res);
		
		return CommonUtils.xmlToMap(res);
	}
	
	//分装微信支付返回结果
	public static  void packageReturnParameter(Integer clientId, String type, PayModel model, WeixinPayConfig config,
			Map<String, String> resp, Map<String, String> result) throws Exception {
		
		//封装支付信息给前台
		if(Constants.NATIVE.equals(type)){
			String urlCode = (String) resp.get("code_url");  
		    result.put("urlCode", urlCode);
		} else if(Constants.JSAPI.equals(type)){
			result.put("timeStamp", System.currentTimeMillis() / 1000 + "");
			result.put("package", "prepay_id="+resp.get("prepay_id"));
			result.put("appId", config.getAppID());
			result.put("nonceStr", resp.get("nonce_str"));
			result.put("signType", "MD5");
			String sign = WXPayUtil.generateSignature(result, config.getKey());
			result.put("paySign", sign);
		}
		result.put("success", "true");
	}
	
	
	public static void main(String[] args) throws DocumentException {
		String s = "<xml><appid>wx2421b1c4370ec43b</appid><customs>ZHENGZHOU_BS</customs><mch_customs_no>D00411</mch_customs_no><mch_id>1262544101</mch_id><order_fee>13110</order_fee><out_trade_no>15112496832609</out_trade_no><product_fee>13110</product_fee><sign>8FF6CEF879FB9555CD580222E671E9D4</sign><transaction_id>1006930610201511241751403478</transaction_id><transport_fee>0</transport_fee><fee_type>CNY</fee_type><sub_order_no>15112496832609001</sub_order_no></xml>";
		System.out.println(CommonUtils.xmlToMap(s));
	}
	
}
