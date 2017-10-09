package com.zm.order.pojo;

public class CustomModel {
	
	private Integer id;

	private String outRequestNo;// 报关流水号,订单号

	private String payNo;// 交易流水号

	private String amount;// 单位为RMB，精确到分（支付宝需要此参数，单位为元）
	
	private Integer centerId;
	
	private Integer payType;

	private String isSplit;// 是否拆单，可空

	private String subOutBizNo;// 拆单时自订单号，可空

	private String buyerName;// 订购人姓名，可空

	private String buyerIdNo;// 订购人身份证信息,可空

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOutRequestNo() {
		return outRequestNo;
	}

	public void setOutRequestNo(String outRequestNo) {
		this.outRequestNo = outRequestNo;
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

	public String getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(String isSplit) {
		this.isSplit = isSplit;
	}

	public String getSubOutBizNo() {
		return subOutBizNo;
	}

	public void setSubOutBizNo(String subOutBizNo) {
		this.subOutBizNo = subOutBizNo;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerIdNo() {
		return buyerIdNo;
	}

	public void setBuyerIdNo(String buyerIdNo) {
		this.buyerIdNo = buyerIdNo;
	}

	@Override
	public String toString() {
		return "CustomModel [outRequestNo=" + outRequestNo + ", payNo=" + payNo + ", amount=" + amount + ", isSplit="
				+ isSplit + ", subOutBizNo=" + subOutBizNo + ", buyerName=" + buyerName + ", buyerIdNo=" + buyerIdNo
				+ "]";
	}

}
