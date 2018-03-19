package com.zm.finance.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	// *********************资金池redis前缀*********************

	public static final String CAPITAL_PERFIX = "capital:";
	public static final String CAPITAL_DETAIL = "capitaldetail:";

	// *********************区域中心返佣***************************
	public static final String CENTER_ORDER_REBATE = "center:orderrebate:";
	// *********************店铺返佣***************************
	public static final String SHOP_ORDER_REBATE = "shop:orderrebate:";
	// *********************推手返佣***************************
	public static final String PUSHUSER_ORDER_REBATE = "pushuser:orderrebate:";
	// *********************返佣详情***************************
	public static final String REBATE_DETAIL = "rebatedetail:";
	
	// *********************BUSSINESSTYPE***************************
	public static final Integer CASH = 0;
	public static final Integer REBATE = 1;
	public static final Integer PRESENT = 2;
	public static final Integer COUPON = 3;
}
