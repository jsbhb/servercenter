package com.zm.pay.pojo;

import java.io.Serializable;
/**
 * 支付宝支付 除了退款不支持MD5用RSA，其他用MD5
 * @author user
 *
 */
public class AliPayConfigModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer centerId;
	//合作者ID
	private String pid;
	//应用ID
	private String appId;
	
	private String key;
	
	private String rsaPrivateKey;
	
	private String rsaPublicKey;
	
	private String signType;
	
	private String charset;
	
	private String url;

	public void initParameter(){
		if(signType == null){
			signType = "MD5";
		}
		if(charset == null){
			charset = "utf-8";
		}
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
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
