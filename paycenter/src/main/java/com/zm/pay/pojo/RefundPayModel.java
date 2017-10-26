package com.zm.pay.pojo;

public class RefundPayModel {

	private String orderId;

	private String payNo;

	private String amount;

	private String reason;

	public String getBizContent() {
		return "{" + "\"out_trade_no\":" + orderId + "," + "\"trade_no\":" + payNo + "," + "\"refund_amount\":" + amount
				+ "," + "\"refund_reason\":\"正常退款\"}";
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
