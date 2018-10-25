package com.zm.finance.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	// *********************资金池redis前缀*********************

	public static final String CAPITAL_PERFIX = "capital:";
	public static final String CAPITAL_DETAIL = "capitaldetail:";

	// *********************GRADE返佣***************************
	public static final String GRADE_ORDER_REBATE = "grade:orderrebate:";
	// *********************返佣详情***************************
	public static final String REBATE_DETAIL = "rebatedetail:";

	// *********************BUSSINESSTYPE业务类型0:现金,1:返佣,2:赠送,3:抵用券,4:清算***************************
	public static final Integer CASH = 0;
	public static final Integer CREDIT = 1;
	public static final Integer ORDER_CONSUMPTION = 2;
	public static final Integer LIQUIDATION = 3;
	public static final Integer REBATE = 4;
	
	// *********************PAYTYPE 0:收入，1：支出***************************
	public static final Integer INCOME = 0;
	public static final Integer EXPENDITURE = 1;
	
	
	//**********************************************************************//
	public static final String CAN_BE_PRESENTED = "canBePresented";//待对账
	public static final String ALREADY_CHECK = "alreadyCheck";//已对账，可使用
	public static final String ALREADY_PRESENTED = "alreadyPresented";//已提现
}
