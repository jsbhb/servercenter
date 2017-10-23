package com.zm.order.pojo;

public class OrderExpress {

	private Integer id;
	
	private String orderId;
	
	private String expressKey;
	
	private String expressName;
	
	private String expressId;

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
