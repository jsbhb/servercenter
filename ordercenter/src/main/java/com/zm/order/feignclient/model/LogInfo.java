package com.zm.order.feignclient.model;

/**  
 * ClassName: LogInfo <br/>  
 * Function: 日志类POJO. <br/>   
 * date: Aug 16, 2017 10:14:43 AM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class LogInfo {

	private Integer id;
	
	private Integer centerId;
	
	private String centerName;
	
	private Integer clientId;
	
	private String apiName;
	
	private Integer apiId;
	
	private String content;
	
	private String createTime;
	
	private String opt;

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

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
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

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	@Override
	public String toString() {
		return "LogInfo [id=" + id + ", centerId=" + centerId + ", centerName=" + centerName + ", clientId=" + clientId
				+ ", apiName=" + apiName + ", apiId=" + apiId + ", content=" + content + ", createTime=" + createTime
				+ ", opt=" + opt + "]";
	}
	
}
