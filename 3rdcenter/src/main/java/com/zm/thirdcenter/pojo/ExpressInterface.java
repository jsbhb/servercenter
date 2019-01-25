package com.zm.thirdcenter.pojo;

import java.io.Serializable;

public class ExpressInterface implements Serializable{
	
	/**  
	 * serialVersionUID:快递公司信息配置.  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = -614502713909900994L;

	private Integer id;
	private String expressCode;
	private String targetObject;
	private String appKey;
	private String appSecret;
	private String url;
	private String tradeId;
	private double version;
	private String buzType;
	private String format;
	private int retryLimit;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	public String getBuzType() {
		return buzType;
	}
	public void setBuzType(String buzType) {
		this.buzType = buzType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public int getRetryLimit() {
		return retryLimit;
	}
	public void setRetryLimit(int retryLimit) {
		this.retryLimit = retryLimit;
	}
}
