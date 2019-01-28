package com.zm.thirdcenter.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoteModel implements Serializable{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = -5539932486085663036L;

	@JsonProperty("ShipperCode")
	private String shipperCode;//快递公司编码
	
	@JsonProperty("Success")
	private String success;
	
	@JsonProperty("LogisticCode")
	private String logisticCode;//快递单号
	
	@JsonProperty("State")
	private String state;
	
	@JsonProperty("EBusinessID")
	private String eBusinessID;
	
	@JsonProperty("Traces")
	private List<TracesModel> traces;
	
	@JsonProperty("Reason")
	private String reason;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String geteBusinessID() {
		return eBusinessID;
	}
	public void seteBusinessID(String eBusinessID) {
		this.eBusinessID = eBusinessID;
	}
	public String getShipperCode() {
		return shipperCode;
	}
	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getLogisticCode() {
		return logisticCode;
	}
	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<TracesModel> getTraces() {
		return traces;
	}
	public void setTraces(List<TracesModel> traces) {
		this.traces = traces;
	}
	@Override
	public String toString() {
		return "RoteModel [shipperCode=" + shipperCode + ", success=" + success + ", logisticCode=" + logisticCode
				+ ", state=" + state + ", traces=" + traces + "]";
	}
	
	
}
