package com.zm.order.log.model;

public class OpenInfLog {

	private Integer centerId;
	private Integer serverId;
	private String serverName;
	private String methodName;
	private String callIp;
	private String content;
	private String parameter;
	private String createTime;

	public OpenInfLog(Integer centerId, Integer serverId, String serverName, String methodName, String callIp,
			String parameter) {
		this.centerId = centerId;
		this.serverId = serverId;
		this.serverName = serverName;
		this.methodName = methodName;
		this.callIp = callIp;
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCallIp() {
		return callIp;
	}

	public void setCallIp(String callIp) {
		this.callIp = callIp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
