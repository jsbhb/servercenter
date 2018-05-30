package com.zm.order.pojo;

public class ButtJointOrder extends OrderInfo {

	private String name;

	private String numId;

	private String phone;

	public boolean validate() {
		return (name != null && orderFlag != null && orderId != null && expressType != null && centerId != null
				&& supplierId != null && tdq != null && orderSource != null && weight != null && createType != null
				&& numId != null && phone != null && shopId != null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumId() {
		return numId;
	}

	public void setNumId(String numId) {
		this.numId = numId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
