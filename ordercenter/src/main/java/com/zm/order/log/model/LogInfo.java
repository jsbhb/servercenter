package com.zm.order.log.model;

public class LogInfo {

	private Integer centerId;
	private Integer shopId;
	private String parameter;
	private Integer source;
	private Integer type;
	private String content;
	private String createTime;
	private String callIp;
	private String opt;
	private String methodName;
	private Integer serverId;

	public LogInfo(String parameter, Integer source, Integer type, String callIp, String methodName, String content,
			Integer serverId) {
		this.parameter = parameter;
		this.source = source;
		this.type = type;
		this.content = content;
		this.callIp = callIp;
		this.methodName = methodName;
		this.serverId = serverId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

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

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
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

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
