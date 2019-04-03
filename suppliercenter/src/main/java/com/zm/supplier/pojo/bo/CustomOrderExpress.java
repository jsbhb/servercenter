package com.zm.supplier.pojo.bo;
/**
 * @fun 订单对应的物流企业信息，用于申报海关
 * @author user
 *
 */
public class CustomOrderExpress {

	private String orderId;
	private String jsonStr;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
}
