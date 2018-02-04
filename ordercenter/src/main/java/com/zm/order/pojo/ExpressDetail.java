package com.zm.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExpressDetail {

	@JsonIgnore
	private String id;
	private String expressName;
	private String expressId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
}
