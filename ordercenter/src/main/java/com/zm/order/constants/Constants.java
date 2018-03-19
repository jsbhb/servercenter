package com.zm.order.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;

	public static final Integer OWN_SUPPLIER = 1;// 自营仓ID

	// *********************各支付类型******************************/
	public static final String WX_PAY = "1";

	public static final String ALI_PAY = "2";

	public static final String UNION_PAY = "3";

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
	public static final Integer CAPITAL_POOL_ENOUGH = 12;// 资金池已经扣减，待推送仓库
	public static final Integer ORDER_EXCEPTION = 99;// 订单异常

	// *********************活动范围，获取是否有全场活动******************************/
	public static final Integer ACTIVE_AREA = 1;// 活动范围：全场

	// *********************活动类型******************************/
	public static final Integer FULL_CUT = 1;// 满减

	public static final Integer FULL_DISCOUNT = 2;// 满打折

	// *******************区域中心ID**********************

	public static final Integer O2O_CENTERID = 1;

	public static final Integer BIG_TRADE_CENTERID = 2;

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

	// *********************资金池redis前缀*********************

	public static final String CAPITAL_PERFIX = "capital:";
	public static final String CAPITAL_DETAIL = "capitaldetail:";

	// *********************商品返佣redis前缀***************************
	public static final String GOODS_REBATE = "goodsrebate:";

	// *********************订单返佣redis前缀***************************
	public static final String ORDER_REBATE = "orderrebate:";

	// *********************区域中心返佣***************************
	public static final String CENTER_ORDER_REBATE = "center:orderrebate:";
	// *********************店铺返佣***************************
	public static final String SHOP_ORDER_REBATE = "shop:orderrebate:";
	// *********************推手返佣***************************
	public static final String PUSHUSER_ORDER_REBATE = "pushuser:orderrebate:";
	// *********************返佣详情***************************
	public static final String REBATE_DETAIL = "rebatedetail:";
}
