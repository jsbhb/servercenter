package com.zm.order.pojo;

public class OrderIdAndSupplierId {
	
	private Integer id;
	
	private String orderId;

	private String thirdOrderId;

	private Integer supplierId;

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

	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

}
