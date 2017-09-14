package com.zm.order.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;
	
	
	public static final Integer OWN_SUPPLIER = 1;//自营仓ID
	
	public static final Integer O2O_ORDER_TYPE = 0;
	
	public static final Integer TRADE_ORDER_TYPE = 1;
	
	
	//*********************各支付类型******************************/
	public static final String WX_PAY = "1";
	
	public static final String ALI_PAY = "2";
	
	//*********************订单状态******************************/
	
	public static final Integer ORDER_INIT = 0;//初始
	public static final Integer ORDER_PAY = 1;//已付款
	public static final Integer ORDER_TO_WAREHOUSE = 2;//已发仓库
	public static final Integer ORDER_CUSTOMS = 3;//已报海关
	public static final Integer ORDER_DZFX = 4;//单证放行
	public static final Integer ORDER_DELIVER = 5;//已发货
	public static final Integer ORDER_COMPLETE = 6;//已收货订单完成
	public static final Integer ORDER_CANCEL = 7;//退单
}
