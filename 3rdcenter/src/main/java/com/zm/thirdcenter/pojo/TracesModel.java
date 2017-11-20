package com.zm.thirdcenter.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TracesModel implements Serializable{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1731075283428881981L;

	@JsonProperty("AcceptTime")
	private String acceptTime;//时间
	
	@JsonProperty("AcceptStation")
	private String acceptStation;//时间+地址+派送人姓名
	
	private String remark;
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	public String getAcceptStation() {
		return acceptStation;
	}
	public void setAcceptTtation(String acceptStation) {
		this.acceptStation = acceptStation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "TracesModel [acceptTime=" + acceptTime + ", acceptStation=" + acceptStation + ", remark=" + remark
				+ "]";
	}
	
}
