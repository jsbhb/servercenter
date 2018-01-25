package com.zm.supplier.supplierinf.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiangYouOrderStatusTemp {

	@JsonProperty("LogisticsNo")
	private String logisticsNo;
	
	@JsonProperty("LogisticsName")
	private String logisticsName;
	
	@JsonProperty("message")
	private List<LiangYouOrderStatusSubTemp> message;
	
	public void sortList(){
		Collections.sort(message);
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public List<LiangYouOrderStatusSubTemp> getMessage() {
		return message;
	}

	public void setMessage(List<LiangYouOrderStatusSubTemp> message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "LiangYouOrderStatusTemp [logisticsNo=" + logisticsNo + ", logisticsName=" + logisticsName + ", message="
				+ message + "]";
	}
	
}
