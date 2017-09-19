package com.zm.pay.bussiness.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.wxpay.sdk.WXPayUtil;
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
			@RequestBody PayModel model, HttpServletRequest req) throws Exception {

		WeixinPayConfig config = (WeixinPayConfig) redisTemplate.opsForValue()
				.get(Constants.PAY + clientId + Constants.WX_PAY);

		config.setHttpConnectTimeoutMs(5000);
		config.setHttpReadTimeoutMs(5000);

		Map<String, String> resp = WxPayUtils.unifiedOrder(type, config, model);

		String return_code = (String) resp.get("return_code");
		Map<String, String> result = new HashMap<String, String>();

		if ("SUCCESS".equals(return_code)) {
			String content = "订单号 \"" + model.getOrderId() + "\" 通过微信支付"+type+"，后台请求成功";
			logFeignClient.saveLog(Constants.FIRST_VERSION,
					CommonUtils.packageLog(LogConstants.WX_PAY, "微信支付", clientId, content, ""));
			
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
		} else {
			result.put("success", "false");
		}

		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
	}
}
