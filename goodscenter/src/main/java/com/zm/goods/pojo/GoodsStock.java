package com.zm.goods.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GoodsStock {

	@JsonIgnore
	private Integer id;
	
	private String itemId;
	
	private Integer quantity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
}
