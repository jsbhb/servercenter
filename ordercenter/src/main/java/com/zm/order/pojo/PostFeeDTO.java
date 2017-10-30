package com.zm.order.pojo;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PostFeeDTO {

	private Double price;
	
	private String province;
	
	private String expressKey;
	
	private Integer weight;
	
	private Integer centerId;
	
	public PostFeeDTO(){}
	
	public PostFeeDTO(Double price,String province, Integer weight, Integer centerId){
		this.price = price;
		this.province = province;
		this.weight = weight;
		this.centerId = centerId;
	}

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
