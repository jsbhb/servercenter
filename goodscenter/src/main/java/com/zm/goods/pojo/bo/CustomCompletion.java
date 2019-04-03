package com.zm.goods.pojo.bo;
/**
 * @fun 用于海关申报，补全商品信息
 * @author user
 *
 */
public class CustomCompletion {
	private String itemId;
	private String origin;
	private String hscode;
	private String brand;
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getHscode() {
		return hscode;
	}
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
}
