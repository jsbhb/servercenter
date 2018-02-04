package com.zm.auth.model;

public class GetTokenParam {

	private Double version;
	private String appKey;
	private String nonceStr;
	private String sign;

	public boolean validata() {
		return (version != null && appKey != null && nonceStr != null && sign != null);
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
