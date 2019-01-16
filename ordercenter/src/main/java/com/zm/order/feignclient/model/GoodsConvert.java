package com.zm.order.feignclient.model;

public class GoodsConvert {

	private Integer conversion;
	private String sku;
	private String itemId;
	private String carton;
	public Integer getConversion() {
		return conversion;
	}
	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getCarton() {
		return carton;
	}
	public void setCarton(String carton) {
		this.carton = carton;
	}
	
}
