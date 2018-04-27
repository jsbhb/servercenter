package com.zm.order.feignclient.model;

public class OrderBussinessModel {

	private String orderId;
	
	private String itemId;
	
	private String itemCode;
	
	private String sku;
	
	private Integer quantity;
	
	private String deliveryPlace;
	
	private Double itemPrice;

	private Double actualPrice;

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getDeliveryPlace() {
		return deliveryPlace;
	}

	public void setDeliveryPlace(String deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}
	
}
