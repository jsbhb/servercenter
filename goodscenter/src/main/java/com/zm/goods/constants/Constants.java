package com.zm.goods.constants;

public class Constants {

	public static final Double FIRST_VERSION = 1.0;
	
	public static final Integer BIG_TRADE = 0;//大贸
	
	public static final Integer CROSS_BORDER = 1;//跨境
	
	public static final Integer ACTIVE_AREA = 1;//活动范围：全场
	
	public static final Integer ACTIVE_MODEL = 1;//活动模块
	
	//*********************活动状态************************
	public static final Integer ACTIVE_START = 1;
	
	public static final Integer ACTIVE_UNSTART = 0;
	
	//********************LUCENE*************************
	public static final String TOTAL = "total";
	
	public static final String ID_LIST = "idList";
	
	public static final String HIGHLIGHTER_MODEL = "highlighterModel";
	
	public static final String BRAND = "brand";
	
	public static final String BRAND_PY = "brandpy";
	
	public static final String ORIGIN = "origin";
	
	public static final String PROPORTION = "proportion";
	
	//*********************订单类型*********************
	
	public static final Integer O2O_ORDER = 0;
	
	//*********************订货平台ID*********************
	
	public static final Integer PREDETERMINE_PLAT_TYPE = -1;
	
	//*********************商品返佣redis前缀***************************
	public static final String GOODS_REBATE = "goodsrebate:";
	
	//*********************包邮包税redis前缀***************************
	public static final String POST_TAX = "post_tax:";
	
	//*********************商品缓存***********************************
	public static final String GOODS_CACHE = "goodscache:";
	
	//*******************eshopgoodsoperationtype**********************
	public static final Integer OPERATION_TYPE_PURCHASE_INSTOCK = 101;
	public static final Integer OPERATION_TYPE_INVENTORY_PROFIT = 301;
	public static final Integer OPERATION_TYPE_INVENTORY_LOSSES = 302;
	
	//************************redis前缀********************************//
	public static final String BUTT_JOINT_USER_PREFIX = "buttjoint:";//对接用户
	
	public static final String GRADEBO_INFO = "gradeBO:";//每个ID的gradeBO信息
	
	//************************福利网站的平台来源ID********************************//
	public static final int WELFARE_WEBSITE = 7;
	
	public static final Integer CNCOOPBUY = 2;
	//************************商品上下架记录管理********************************//
	public static final int GOODS_SHELVE_TYPE_UP = 0;
	public static final int GOODS_SHELVE_TYPE_DOWN = 1;
	public static final int GOODS_SHELVE_MODE_AUTO = 0;
	
}
