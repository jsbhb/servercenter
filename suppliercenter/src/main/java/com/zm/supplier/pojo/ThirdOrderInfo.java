package com.zm.supplier.pojo;

public class ThirdOrderInfo {

	private Integer id;
	
	private String orderId;
	
	private String expressKey;
	
	private String thirdOrderId;
	
	private String status;
	
	private Integer orderStatus;
	
	private String expressName;
	
	private String expressId;
	
	private String mftNo;
	
	private String remark;

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
		return "ThirdOrderInfo [id=" + id + ", orderId=" + orderId + ", expressKey=" + expressKey + ", thirdOrderId="
				+ thirdOrderId + ", status=" + status + ", orderStatus=" + orderStatus + ", expressName=" + expressName
				+ ", expressId=" + expressId + ", mftNo=" + mftNo + ", remark=" + remark + "]";
	}
	
}
