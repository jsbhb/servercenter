package com.zm.pay.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	// ****************************微信支付参数*******************************

	public static final String FEE_TYPE = "CNY";

//	public static final String WX_NOTIFY_URL = "http://api.cncoopbuy.com/paycenter/auth/payMng/wxPayReturn";//正式

	public static final String WX_NOTIFY_URL = "http://106.15.156.118//paycenter/auth/payMng/wxPayReturn";//测试

	public static final String JSAPI = "JSAPI";

	public static final String MWEB = "MWEB";

	public static final String APP = "APP";

	public static final String NATIVE = "NATIVE";

	public static final Integer HTTPCONNECTTIMEOUTMS = 5000;

	public static final Integer HTTPREADTIMEOUTMS = 5000;

	public static final String CREATE_IP = "127.0.0.1";

	// *********************各支付类型******************************/

	public static final String PAY = "PAY";// 前缀，和登录config分开

	public static final Integer WX_PAY = 1;

	public static final Integer ALI_PAY = 2;

	public static final Integer UNION_PAY = 3;
	
	public static final String UNION_PAY_MER_ID = "unionpay:mer_id";

	// *********************订单状态******************************/

	public static final Integer ORDER_INIT = 0;// 初始
	public static final Integer ORDER_PAY = 1;// 已付款
	public static final Integer ORDER_PAY_CUSTOMS = 2;// 支付单报关
	public static final Integer ORDER_TO_WAREHOUSE = 3;// 已发仓库
	public static final Integer ORDER_CUSTOMS = 4;// 已报海关
	public static final Integer ORDER_DZFX = 5;// 单证放行
	public static final Integer ORDER_DELIVER = 6;// 已发货
	public static final Integer ORDER_COMPLETE = 7;// 已收货订单完成
	public static final Integer ORDER_CANCEL = 8;// 退单
	public static final Integer ORDER_CLOSE = 9;// 交易关闭

	// *********************支付有效时间******************************/
	public static final Integer PAY_EFFECTIVE_TIME_HOUR = 1;

	// *********************支付报关参数******************************/
	public static final String MERCHANT_CUSTOMS_CODE = "3302462230";// 商户在海关备案的编号。
	public static final String MERCHANT_CUSTOMS_NAME = "宁波鑫海通达贸易有限公司";// 商户海关备案名称。
	public static final String CUSTOMS_PLACE = "NINGBO";// 海关编号
	public static final String CUSTOMS_SERVICE = "alipay.acquire.customs";// 报关接口
	public static final String CUSTOMS_QUERY_SERVICE = "alipay.overseas.acquire.customs.query";// 报关接口

	// ********************支付宝支付参数*****************************
//	public static final String ALI_NOTIFY_URL = "http://api.cncoopbuy.com/paycenter/auth/payMng/payNotify";//正式
//	public static final String ALI_RETURN_URL = "http://api.cncoopbuy.com/paycenter/auth/payMng/payReturn";//正式
	public static final String ALI_NOTIFY_URL = "http://106.15.156.118/paycenter/auth/payMng/payNotify";//测试
	public static final String ALI_RETURN_URL = "http://106.15.156.118/paycenter/auth/payMng/payReturn";//测试
	public static final String SCAN_CODE = "scanCode";// 扫码支付
	public static final String SCAN_CODE_SERVICE = "create_direct_pay_by_user";// 扫码支付接口名

	// *********************银联公共参数********************************
	/** signMethod，没配按01*/
	public static final String SIGN_METHOD = "01";
	/** version，没配按5.0.0 */
	public static final String VERSION = "5.1.0";
	
	/** 是否验证验签证书CN，除了false都验 测试环境请设置false，生产环境请设置true。非false的值默认都当true处理。*/
	public static final boolean IF_VALIDATE_CNNAME = false;

	// 后台服务对应的写法参照 BackRcvResponse.java
	public static String BACK_URL = "http://106.15.156.118/paycenter/auth/payMng/unionNotify";//测试
	public static String FRONT_URL = "http://testapi.cncoopbuy.com/paycenter/auth/payMng/union-frontrcv";//测试
//	public static String FRONT_URL = "http://api.cncoopbuy.com/paycenter/auth/payMng/union-frontrcv";//正式
//	public static String BACK_URL = "http://api.cncoopbuy.com/paycenter/auth/payMng/unionNotify";//正式

}
