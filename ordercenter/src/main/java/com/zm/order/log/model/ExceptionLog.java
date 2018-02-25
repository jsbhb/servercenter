package com.zm.order.log.model;

/**
 * ClassName: LogInfo <br/>
 * Function: 异常日志类POJO. <br/>
 * date: Aug 16, 2017 10:14:43 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class ExceptionLog {

	private Integer serverId;

	private String serverName;

	private String methodName;

	private String content;

	public ExceptionLog(Integer serverId, String serverName, String methodName, String content) {
		this.content = content;
		this.serverId = serverId;
		this.serverName = serverName;
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
