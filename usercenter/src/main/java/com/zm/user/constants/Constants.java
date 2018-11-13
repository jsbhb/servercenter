package com.zm.user.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	public static final String EMAIL = "email";

	public static final String MOBILE = "phone";

	public static final String ACCOUNT = "account";

	public static final Integer PHONE_VERIFY_ERROR_CODE = 1;

	// *********************各支付类型******************************/
	public static final String WX_PAY = "1";

	public static final String ALI_PAY = "2";
	
	public static final String JSAPI = "JSAPI";

	// *********************第三方登录类型******************************/

	public static final int WX_LOGIN = 1;

	public static final int QQ_LOGIN = 2;

	public static final int SINABLOG_LOGIN = 3;
	
	public static final int WX_APPLET_LOGIN = 5;
	
	// *********************区域中心ID,O2O=1;大贸=2******************************/

	public static final Integer O2O_CENTER_ID = 1;
	
	public static final Integer BIG_TRADE_CENTER_ID = 2;
	
	// *********************用户类型******************************/
	
	public static final Integer COOP = 1;
	
	public static final Integer CENTER = 2;
	
	public static final Integer SHOP = 3;
	
	public static final Integer SHOPPING_GUIDE = 4;
	
	
	// *********************推手状态******************************/
	
	public static final Integer AUDIT_PASS = 2;
	public static final Integer AUDIT_UN_PASS = 1;
	
	//*********************分级类型**************************
	public static final Integer AREA_CENTER = 2;//区域中心
	
	//************************客户类型**************//
	public static final Integer BUTT_JOINT_USER = 2;//对接用户
	
	public static final String BUTT_JOINT_USER_PREFIX = "buttjoint:";//对接用户
	
	public static final String GRADEBO_INFO = "gradeBO:";//每个ID的gradeBO信息
	
	//*********************发送邀请码状态**************************
	public static final Integer UN_SEND = 1;
	public static final Integer SEND = 2;
	public static final Integer BIND = 3;
	public static final Integer INVALID = 4;
	public static final Integer SEND_ERROR = 5;
}
