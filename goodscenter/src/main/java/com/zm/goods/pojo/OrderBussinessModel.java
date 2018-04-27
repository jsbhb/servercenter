package com.zm.goods.pojo;

public class OrderBussinessModel {
	
	private Integer id;

	private String orderId;
	
	private String sku;
	
	private String itemCode;
	
	private Integer supplierId;
	
	private String itemId;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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

	@Override
	public String toString() {
		return "OrderBussinessModel [orderId=" + orderId + ", sku=" + sku + ", itemCode=" + itemCode + ", itemId="
				+ itemId + ", quantity=" + quantity + ", deliveryPlace=" + deliveryPlace + "]";
	}
	
}
