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

	// *********************BUSSINESSTYPE业务类型***************************
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
	public static final String FROZEN_REBATE = "frozenRebate";//冻结
	public static final String STAY_TO_ACCOUNT = "stayToAccount";//待到账
	public static final String ORDER_CONSUME = "orderConsume";//订单消费
	public static final String CAPITAL_MONEY = "money";
	public static final String CAPITAL_FROZEN_MONEY = "frozenMoney";
	public static final String CAPITAL_USE_MONEY = "useMoney";
	public static final String CAPITAL_COUNT_MONEY = "countmoney";
}
