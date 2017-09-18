package com.zm.order.feignclient.model;

import java.io.Serializable;

public class WXLoginConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer centerId;
	
	private Integer loginType;
	
	private String appId;
	
	private String secret;
	
	private Integer platUserType;
	
	private boolean snsapiBase;
	
	private String redirectUrl;
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public Integer getPlatUserType() {
		return platUserType;
	}

	public void setPlatUserType(Integer platUserType) {
		this.platUserType = platUserType;
	}

	public boolean isSnsapiBase() {
		return snsapiBase;
	}

	public void setSnsapiBase(boolean snsapiBase) {
		this.snsapiBase = snsapiBase;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "WXLoginConfig [id=" + id + ", centerId=" + centerId + ", loginType=" + loginType + ", appId=" + appId
				+ ", secret=" + secret + ", platUserType=" + platUserType + ", snsapiBase=" + snsapiBase + "]";
	}
}
