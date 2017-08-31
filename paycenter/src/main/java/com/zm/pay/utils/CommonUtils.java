package com.zm.pay.utils;

import com.zm.pay.constants.LogConstants;
import com.zm.pay.feignclient.model.LogInfo;

public class CommonUtils {
	
	public static  LogInfo packageLog(Integer apiId, String apiName, Integer clientId, String content, String opt) {
		LogInfo info = new LogInfo();

		info.setApiId(apiId);
		info.setApiName(apiName);
		info.setCenterId(LogConstants.PAYMENT_CENTER_ID);
		info.setCenterName("支付中心");
		info.setClientId(clientId);
		info.setContent(content);
		info.setOpt(opt);

		return info;
	}
}
