package com.zm.pay.pojo;

public class PayModel {

	private String body;

	private String subject;

	private String totalAmount;

	private String orderId;

	private String detail;
	
	//易宝支付需要
	private String phone;

	// 微信相关
	private String openId;

	private String IP;
	// end

	public String getBizContent() {
		return "{" + "\"out_trade_no\":" + orderId + "," + "\"total_amount\":" + totalAmount + "," + "\"subject\":"
				+ subject + "," + "\"body\":" + body + "," + "\"timeout_express\":\"15m\"}";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
