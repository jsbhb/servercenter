package com.zm.supplier.supplierinf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResult {

	@JsonProperty("message")
	private String message;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("data")
	private AccessToken token;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public AccessToken getToken() {
		return token;
	}

	public void setToken(AccessToken token) {
		this.token = token;
	}
	
}
