package com.zm.order.pojo.dto;

public class PostFee {

	private Double price;
	
	private String province;
	
	private String expressKey;
	
	private Integer weight;
	
	private Integer centerId;

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getExpressKey() {
		return expressKey;
	}

	public void setExpressKey(String expressKey) {
		this.expressKey = expressKey;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
}
