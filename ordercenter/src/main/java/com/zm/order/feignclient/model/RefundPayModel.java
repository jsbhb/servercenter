package com.zm.order.feignclient.model;

public class RefundPayModel {

	private String orderId;

	private String payNo;

	private String amount;

	private String reason;

	public RefundPayModel(String orderId, String payNo, String amount, String reason){
		this.orderId = orderId;
		this.payNo = payNo;
		this.amount = amount;
		this.reason = reason;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
