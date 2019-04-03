package com.zm.thirdcenter.bussiness.customs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @fun 上传实时支付数据
 * @author user
 *
 */
@JsonInclude(Include.NON_NULL) 
public class CustomsRealTimeDataUp {

	private String sessionID;
	private PayExchangeInfoHead payExchangeInfoHead;
	private List<PayExchangeInfo> payExchangeInfoLists;
	private String serviceTime;
	private String certNo;
	private String signValue;
	public void encode(){
		payExchangeInfoHead.encode();
		for(PayExchangeInfo info : payExchangeInfoLists){
			info.encode();
		}
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public PayExchangeInfoHead getPayExchangeInfoHead() {
		return payExchangeInfoHead;
	}
	public void setPayExchangeInfoHead(PayExchangeInfoHead payExchangeInfoHead) {
		this.payExchangeInfoHead = payExchangeInfoHead;
	}
	public List<PayExchangeInfo> getPayExchangeInfoLists() {
		return payExchangeInfoLists;
	}
	public void setPayExchangeInfoLists(List<PayExchangeInfo> payExchangeInfoLists) {
		this.payExchangeInfoLists = payExchangeInfoLists;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
}

