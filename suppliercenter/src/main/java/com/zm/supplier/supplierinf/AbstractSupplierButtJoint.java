package com.zm.supplier.supplierinf;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.UserInfo;

public abstract class AbstractSupplierButtJoint {
	
	protected String appKey;
	
	protected String appSecret;
	
	private static final String XML = "XML";
	private static final String JSON = "JSON";
	
	protected Logger logger = LoggerFactory.getLogger(AbstractSupplierButtJoint.class);
	
	
	public <T> Map<String, Object> renderResult(String result, String format, Class<T> clazz){
		if(XML.equalsIgnoreCase(format)){
			
		}else if(JSON.equalsIgnoreCase(format)){
			
		}
		return null;
	}

	/**
	 * @fun 发送订单给第三方
	 * @param info
	 * @param user
	 * @return
	 */
	public abstract Map<String, Object> sendOrder(OrderInfo info, UserInfo user);

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	
}
