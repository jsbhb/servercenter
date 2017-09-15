package com.zm.pay.pojo;

import java.io.Serializable;

public class AliPayConfigModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer centerId;
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


	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
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
		return "AliPayConfigModel [id=" + id + ", centerId=" + centerId + ", pid=" + pid + ", appId=" + appId
				+ ", rsaPrivateKey=" + rsaPrivateKey + ", rsaPublicKey=" + rsaPublicKey + "]";
	}
	
}
