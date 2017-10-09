package com.zm.pay.utils.ali;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;

import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.CustomModel;
import com.zm.pay.utils.CommonUtils;
import com.zm.pay.utils.ali.util.AlipaySubmit;

public class AliPayUtils {
	
	//支付宝报关接口
	public static Map<String,String> acquireCustom(AliPayConfigModel config, CustomModel custom) throws DocumentException {
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
		String sHtmlText = AlipaySubmit.buildRequest(config, sParaTemp, "get", "确认");
		
		return CommonUtils.xmlToMap(sHtmlText);
	}
}
