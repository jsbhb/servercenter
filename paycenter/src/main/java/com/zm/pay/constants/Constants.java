package com.zm.pay.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	public static final String FEE_TYPE = "CNY";

	public static final String WX_NOTIFY_URL = "http://748c8133.ngrok.io/paycenter/auth/payMng/wxPayReturn";

	public static final String JSAPI = "JSAPI";
	
	public static final String MWEB = "MWEB";
	
	public static final String APP = "APP";
	
	public static final String NATIVE = "NATIVE";

	public static final Integer HTTPCONNECTTIMEOUTMS = 5000;

	public static final Integer HTTPREADTIMEOUTMS = 5000;

	public static final String CREATE_IP = "127.0.0.1";

	// *********************各支付类型******************************/
	
	public static final String PAY = "PAY";//前缀，和登录config分开
	
	public static final String WX_PAY = "1";

	public static final String ALI_PAY = "2";

}
