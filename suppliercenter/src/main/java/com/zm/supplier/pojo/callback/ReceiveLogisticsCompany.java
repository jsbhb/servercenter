package com.zm.supplier.pojo.callback;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReceiveLogisticsCompany {

	@JsonProperty("orderNo")
	private String orderNo;
	
	@JsonProperty("express")
	private String express;
	
	@JsonProperty("logisCompanyName")
	private String logisCompanyName;
	
	@JsonProperty("logisCompanyCode")
	private String logisCompanyCode;
	
	@JsonProperty("express_tracking_number")
	private String expressTrackingNumber;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getLogisCompanyName() {
		return logisCompanyName;
	}

	public void setLogisCompanyName(String logisCompanyName) {
		this.logisCompanyName = logisCompanyName;
	}

	public String getLogisCompanyCode() {
		return logisCompanyCode;
	}

	public void setLogisCompanyCode(String logisCompanyCode) {
		this.logisCompanyCode = logisCompanyCode;
	}

	public String getExpressTrackingNumber() {
		return expressTrackingNumber;
	}

	public void setExpressTrackingNumber(String expressTrackingNumber) {
		this.expressTrackingNumber = expressTrackingNumber;
	}
	
}
