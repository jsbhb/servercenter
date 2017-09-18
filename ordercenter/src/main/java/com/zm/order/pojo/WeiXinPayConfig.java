package com.zm.order.pojo;

public class WeiXinPayConfig extends AbstractPayConfig{

	private String openId;
	
	private String ip;
	
	public WeiXinPayConfig(String openId, String ip){
		this.openId = openId;
		this.ip = ip;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
