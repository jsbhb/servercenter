package com.zm.gateway.model;

public enum ErrorCodeEnum {

	SERVER_ERROR("-1", "服务器异常"), TOKEN_VALIDATE_ERROR("10000", "token validate error"), SIGN_VALIDATE_ERROR("10001",
			"sign签名错误"), REPEAT_ERROR("10002", "重复提交"), NO_GOODS_ERROR("10003", "没有商品信息"), OUT_OF_STOCK("10004",
					"库存不足"), OUT_OF_RANGE("10005", "购买数量不在规定范围"), MISSING_PARAM("10006",
							"缺少必要参数"), ORDER_MISS_DETAIL("10007", "订单缺少orderDetail"), ORDER_MISS_GOODS("10008",
									"订单缺少orderGoodsList"), IDENTIFY_VALIDATE_ERROR("10009",
											"身份证验证失败"), BUYER_PHONE_VALIDATE_ERROR("10010",
													"订购人手机验证失败"), RECEIVE_PHONE_ERROR("10011",
															"收货人手机验证失败"), OUT_OF_PRICE("10012",
																	"跨境订单单笔总价不能超过2000"), GOODS_DOWNSHELVES("10013",
																			"商品已下架"), FORMAT_ERROR("10014",
																					"请使用JSON格式");

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
