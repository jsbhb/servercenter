package com.zm.order.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	// *********************各支付类型******************************/
	public static final String WX_PAY = "1";

	public static final String ALI_PAY = "2";

	public static final String UNION_PAY = "3";
	
	public static final String REBATE_PAY = "4";
	
	public static final String YOP_PAY = "5";

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
	public static final Integer CAPITAL_POOL_NOT_ENOUGH = 11;// 资金池不足
	public static final Integer CAPITAL_POOL_ENOUGH = 12;// 资金池已经扣减,报关/推送仓库
	public static final Integer REFUNDS = 21;// 退款中
	public static final Integer ORDER_EXCEPTION = 99;// 订单异常

	// *********************活动范围，获取是否有全场活动******************************/
	public static final Integer ACTIVE_AREA = 1;// 活动范围：全场

	// *********************活动类型******************************/
	public static final Integer FULL_CUT = 1;// 满减

	public static final Integer FULL_DISCOUNT = 2;// 满打折

	// *******************快递方式******************
	public static final Integer EXPRESS = 0;

	public static final Integer OWN_CARRIER = 1;

	// *********************订单类型*********************

	public static final Integer O2O_ORDER_TYPE = 0;// 跨境

	public static final Integer TRADE_ORDER_TYPE = 1;// 大贸

	public static final Integer GENERAL_TRADE = 2;// 一般贸易

	// ************************分润前缀**********************
	public static final String PROFIT = "profit";

	// *********************异常分润订单***********************
	public static final String EXCEPTION_PROFIT = "EXCEPTION_PROFIT";

	// *********************订货平台订单***********************
	public static final Integer PREDETERMINE_ORDER = 2;

	// *********************订货平台ID*********************

	public static final Integer PREDETERMINE_PLAT_TYPE = -1;

	// *********************中国供销海外购ID*********************

	public static final Integer CNCOOPBUY = 2;

	// *********************资金池redis前缀*********************

	public static final String CAPITAL_PERFIX = "capital:";
	public static final String CAPITAL_DETAIL = "capitaldetail:";

	// *********************商品返佣redis前缀***************************
	public static final String GOODS_REBATE = "goodsrebate:";

	// *********************订单返佣redis前缀***************************
	public static final String ORDER_REBATE = "orderrebate:";

	// *********************等级返佣***************************
	public static final String GRADE_ORDER_REBATE = "grade:orderrebate:";

	// *********************返佣详情***************************
	public static final String REBATE_DETAIL = "rebatedetail:";

	// **********************订单周统计前缀***********************
	public static final String ORDER_STATISTICS_WEEK = "orderstatistics:week:";

	// **********************订单月统计前缀***********************
	public static final String ORDER_STATISTICS_MONTH = "orderstatistics:month:";

	// **********************销售额周统计前缀***********************
	public static final String SALES_STATISTICS_WEEK = "salesstatistics:week:";

	// **********************销售额月统计前缀***********************
	public static final String SALES_STATISTICS_MONTH = "salesstatistics:month:";

	// **********************订单当天统计前缀***********************
	public static final String ORDER_STATISTICS_DAY = "orderstatistics:day:";

	// **********************销售额当天统计前缀***********************
	public static final String SALES_STATISTICS_DAY = "salesstatistics:day:";
	
	//*********************包邮包税redis前缀***************************
	public static final String POST_TAX = "post_tax:";
	
	//*********************包邮包税***************************
	public static final String FREE_POST = "1";
	public static final String ARRIVE_POST = "2";
	public static final String FREE_TAX = "1";
	
	
	//*******************订单来源**********************
	public static final Integer ORDER_SOURCE_YOUZAN = 3;//有赞
	public static final Integer ORDER_SOURCE_EXHIBITION = 5;//展厅
	public static final Integer ORDER_SOURCE_BIG_CUSTOMER = 6;//大客户
	public static final Integer WELFARE_WEBSITE = 7;//福利网站
	public static final int BACK_MANAGER_WEBSITE = 8;//后台来源
	public static final Integer TAIPING_HUIHUI = 9;//太平慧慧
	
	//*******************订单创建类型**********************
	public static final Integer OPEN_INTERFACE_TYPE = 5;//对接订单
	
	//*******************一般贸易仓金额**********************
	public static final Integer GENERAL_TRADE_FEE = 500;
	
	//*******************一般贸易仓ID**********************
	public static final Integer GENERAL_WAREHOUSE_ID = 6;
	
	//*******************金额允许偏离范围**********************
	public static final Integer DEVIATION = 5;
	
	//*******************海关税率折扣**********************
	public static final double TAX_DISCOUNT = 0.7;

}
