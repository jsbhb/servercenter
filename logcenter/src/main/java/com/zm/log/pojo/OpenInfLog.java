package com.zm.log.pojo;

public class OpenInfLog extends Log{

	private Integer centerId;
	private String callIp;

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}


	public String getCallIp() {
		return callIp;
	}

	public void setCallIp(String callIp) {
		this.callIp = callIp;
	}

}
