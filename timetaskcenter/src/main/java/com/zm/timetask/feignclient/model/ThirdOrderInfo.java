package com.zm.timetask.feignclient.model;

public class ThirdOrderInfo {

	private Integer id;
	
	private String orderId;
	
	private String expressKey;
	
	private String thirdOrderId;
	
	private Integer status;
	
	private String expressName;
	
	private String expressId;
	
	private String mftNo;
	
	private String remark;

	public String getMftNo() {
		return mftNo;
	}

	public void setMftNo(String mftNo) {
		this.mftNo = mftNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getExpressKey() {
		return expressKey;
	}

	public void setExpressKey(String expressKey) {
		this.expressKey = expressKey;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	@Override
	public String toString() {
		return "OrderExpress [id=" + id + ", orderId=" + orderId + ", expressKey=" + expressKey + ", expressName="
				+ expressName + ", expressId=" + expressId + "]";
	}
	
}
