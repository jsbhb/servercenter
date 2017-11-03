package com.zm.supplier.pojo;

public class SendOrderResult {

	private String orderId;
	
	private String thirdOrderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	@Override
	public String toString() {
		return "SendOrderResult [orderId=" + orderId + ", thirdOrderId=" + thirdOrderId + "]";
	}
	
}
