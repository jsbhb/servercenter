package com.zm.supplier.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	public static final Integer WX_PAY = 1;

	public static final Integer ALI_PAY = 2;

	public static final Integer UNION_PAY = 3;

	public static final String SUPPLIER_INTERFACE = "SUPPLIER_INF";

	// ************************粮油分不同地区仓库*****************
	public static final Integer LIANGYOU_NB_SUPPLIERID = 2;

	public static final String PAY = "PAY";// 前缀，和登录config分开

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
	public static final Integer CAPITAL_POOL_NOT_ENOUGH = 11;// 资金池不足
	public static final Integer CAPITAL_POOL_ENOUGH = 12;// 资金池已经扣减,报关/推送仓库
	public static final Integer REFUNDS = 21;// 退款中
	public static final Integer ORDER_EXCEPTION = 99;// 订单异常
}
