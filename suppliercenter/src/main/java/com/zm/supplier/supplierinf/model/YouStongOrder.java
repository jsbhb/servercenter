package com.zm.supplier.supplierinf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zm.supplier.pojo.SendOrderResult;

public class YouStongOrder {

	@JsonProperty("Success")
	private boolean success;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("OrderInfo")
	private OrderInfo orderInfo;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public SendOrderResult convert() {
		SendOrderResult result = new SendOrderResult();
		result.setOrderId(orderInfo.getParterOrderNo());
		result.setThirdOrderId(orderInfo.getOrderNo());
		return result;
	}

}

class OrderInfo {

	@JsonProperty("ParterOrderNo")
	private String parterOrderNo;

	@JsonProperty("OrderNo")
	private String orderNo;

	public String getParterOrderNo() {
		return parterOrderNo;
	}

	public void setParterOrderNo(String parterOrderNo) {
		this.parterOrderNo = parterOrderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
