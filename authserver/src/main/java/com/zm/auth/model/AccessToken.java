package com.zm.auth.model;


public class AccessToken {

	private String token;
	private Integer expires;
	
	public AccessToken(String token, Integer expires){
		this.token = token;
		this.expires = expires;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getExpires() {
		return expires;
	}
	public void setExpires(Integer expires) {
		this.expires = expires;
	}
	
}
