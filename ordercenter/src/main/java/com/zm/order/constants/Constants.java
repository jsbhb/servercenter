package com.zm.order.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	public static final Integer OWN_SUPPLIER = 1;// 自营仓ID

	public static final Integer O2O_ORDER_TYPE = 0;

	public static final Integer TRADE_ORDER_TYPE = 1;

	// *********************各支付类型******************************/
	public static final String WX_PAY = "1";

	public static final String ALI_PAY = "2";
	
	public static final String JSAPI = "JSAPI";

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

}
