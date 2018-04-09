package com.zm.log.pojo;

public class LogInfo extends Log{

	private Integer centerId;
	private Integer shopId;
	private Integer source;
	private Integer type;
	private String callIp;
	private String opt;
	
	public Integer getCenterId() {
		return centerId;
	}
	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCallIp() {
		return callIp;
	}
	public void setCallIp(String callIp) {
		this.callIp = callIp;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	
}
