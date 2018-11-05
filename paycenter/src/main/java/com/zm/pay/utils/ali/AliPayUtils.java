package com.zm.pay.utils.ali;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.pojo.PayModel;
import com.zm.pay.pojo.RefundPayModel;
import com.zm.pay.utils.CalculationUtils;
import com.zm.pay.utils.CommonUtils;
import com.zm.pay.utils.ali.util.AlipaySubmit;

public class AliPayUtils {

	// 支付宝报关接口
	public static Map<String, String> acquireCustom(AliPayConfigModel config, CustomModel custom)
			throws Exception {
		config.initParameter();
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", Constants.CUSTOMS_SERVICE);
		sParaTemp.put("partner", config.getPid());
		sParaTemp.put("_input_charset", config.getCharset());
		sParaTemp.put("out_request_no", custom.getOutRequestNo());
		sParaTemp.put("trade_no", custom.getPayNo());
		sParaTemp.put("merchant_customs_code", Constants.MERCHANT_CUSTOMS_CODE);
		sParaTemp.put("merchant_customs_name", Constants.MERCHANT_CUSTOMS_NAME);
		sParaTemp.put("amount", custom.getAmount());
		sParaTemp.put("customs_place", Constants.CUSTOMS_PLACE);

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(config, "", "", sParaTemp);

		return CommonUtils.xmlToMap(sHtmlText);
	}

	// 支付宝支付接口
	public static String aliPay(String type, AliPayConfigModel config, PayModel model) {

		// 把请求参数打包成数组
		config.initParameter();
		Map<String, String> sParaTemp = new HashMap<String, String>();
		if (Constants.SCAN_CODE.equals(type)) {
			sParaTemp.put("service", Constants.SCAN_CODE_SERVICE);
			sParaTemp.put("payment_type", "1");// 扫码支付默认1
		}

		Double totalFee = CalculationUtils.div(Double.valueOf(model.getTotalAmount()), 100);
		
		sParaTemp.put("partner", config.getPid());
		sParaTemp.put("seller_id", config.getPid());
		sParaTemp.put("_input_charset", config.getCharset());
		sParaTemp.put("notify_url", Constants.ALI_NOTIFY_URL);
		sParaTemp.put("return_url", Constants.ALI_RETURN_URL);
		sParaTemp.put("anti_phishing_key", "");// 防钓鱼时间戳
		sParaTemp.put("exter_invoke_ip", "");
		sParaTemp.put("out_trade_no", model.getOrderId());
		sParaTemp.put("subject", model.getBody());
		sParaTemp.put("total_fee", totalFee.toString());
		sParaTemp.put("body", model.getDetail());

		// 建立请求
		return AlipaySubmit.buildRequest(config, sParaTemp, "get", "确认");
	}

	private static final String ALI_API_GATEWAY = "https://openapi.alipay.com/gateway.do";

	// 支付宝退款接口
	public static AlipayTradeRefundResponse aliRefundPay(AliPayConfigModel config, RefundPayModel model)
			throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(ALI_API_GATEWAY, config.getAppId(),
				config.getRsaPrivateKey(), "json", "utf-8", config.getRsaPublicKey(), "RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(model.getBizContent());
		return alipayClient.execute(request);

	}

	public static AlipayTradePrecreateResponse precreate(AliPayConfigModel config, PayModel model) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(ALI_API_GATEWAY, config.getAppId(),
				config.getRsaPrivateKey(), "json", "utf-8", config.getRsaPublicKey(), "RSA2");
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setNotifyUrl(Constants.ALI_NOTIFY_URL);
		request.setBizContent(model.getBizContent());
		return alipayClient.execute(request);
	}
}
