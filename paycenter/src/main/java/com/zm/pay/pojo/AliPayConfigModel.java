package com.zm.pay.pojo;

public class AliPayConfigModel {

	private Integer id;
	
	private Integer clientId;
	//合作者ID
	private String pid;
	//应用ID
	private String appId;
	
	private String rsaPrivateKey;
	
	private String rsaPublicKey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

	public String getRsaPublicKey() {
		return rsaPublicKey;
	}

	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}

	@Override
	public String toString() {
		return "AliPayConfigModel [id=" + id + ", clientId=" + clientId + ", pid=" + pid + ", appId=" + appId
				+ ", rsaPrivateKey=" + rsaPrivateKey + ", rsaPublicKey=" + rsaPublicKey + "]";
	}
	
}
