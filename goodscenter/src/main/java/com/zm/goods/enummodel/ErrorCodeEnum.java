package com.zm.goods.enummodel;

public enum ErrorCodeEnum {

	SERVER_ERROR("-1", "服务器异常"), TOKEN_VALIDATE_ERROR("10000", "token权限验证错误"),
	SIGN_VALIDATE_ERROR("10001","sign签名错误"),
	REPEAT_ERROR("10002", "重复提交"),
	NO_DATA_ERROR("10003", "没有查到数据"),
	OUT_OF_STOCK("10004","库存不足"),
	OUT_OF_RANGE("10005", "购买数量不在规定范围"),
	MISSING_PARAM("10006","缺少必要参数"),
	ORDER_MISS_DETAIL("10007", "订单缺少orderDetail"),
	ORDER_MISS_GOODS("10008","订单缺少orderGoodsList"),
	IDENTIFY_VALIDATE_ERROR("10009","身份证验证失败"),
	BUYER_PHONE_VALIDATE_ERROR("10010","订购人手机验证失败"),
	RECEIVE_PHONE_ERROR("10011","收货人手机验证失败"),
	OUT_OF_PRICE("10012","跨境订单单笔总价不能超过2000"),
	GOODS_DOWNSHELVES("10013","商品已下架"),
	FORMAT_ERROR("10014","请使用JSON格式"),
	VERSION_ERROR("10015","版本错误"),
	NUMBER_FORMAT_ERROR("10016","请检查字段类型是否正确"),
	PARAM_ERROR("10017","参数有误"),
	RETAIL_PRICE_ERROR("10018","零售价有误"),
	PAYMENT_ERROR("10019","“商品总额+税费+运费”与“支付金额”不匹配"),
	TAX_ERROR("10020","“关税+增值税+消费税”与“总税费”不匹配"),
	SUPPLIER_GOODS_ERROR("10021","订单商品中的商品不属于同一个仓库,请进行拆单处理"),
	PAYMENT_VALIDATE_ERROR("10022","支付金额后台校验不通过，确认税费等费用是否计算正确"),
	TAX_SET_ERROR("10023","后台税率设置有问题"),
	TIME_FORMATE_ERROR("10024","时间格式错误，请使用yyyy-MM-dd HH:mm:ss"),
	EXCEED_MAX_SIZE("10025","批量获取最大限制100个"),
	TYPE_ERROR("10026","订单属性和订单商品属性不同，或订单商品里含有跨境和一般贸易商品");

	private String errorCode;
	private String errorMsg;

	private ErrorCodeEnum(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
