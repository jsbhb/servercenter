package com.zm.pay.utils.wx;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
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
		System.out.println(resp);
		return resp;
	}
	
	public static  void packageReturnParameter(Integer clientId, String type, PayModel model, WeixinPayConfig config,
			Map<String, String> resp, Map<String, String> result) throws Exception {
		
		//封装支付信息给前台
		if(Constants.NATIVE.equals(type)){
			String urlCode = (String) resp.get("code_url");  
		    // 需要修改为运行机器上的路径
		    String path = System.getProperty("user.dir")+"\\resource\\public";
		    File newFile = new File(path);
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
		    String filePath = String.format(path+"/qr-%s.png",
		    		model.getOrderId());
		    ZxingUtils.getQRCodeImge(urlCode, 256, filePath);
		    result.put("qrFile", "http://192.168.199.194:8888/qr-"+model.getOrderId() + ".png");
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
	
	
}
