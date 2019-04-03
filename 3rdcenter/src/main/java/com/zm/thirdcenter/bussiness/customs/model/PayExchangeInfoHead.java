package com.zm.thirdcenter.bussiness.customs.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @fun 海关实时支付信息头
 * @author user
 *
 */
@JsonInclude(Include.NON_NULL) 
public class PayExchangeInfoHead {
	private String guid;
	private String initalRequest;
	private String initalResponse;
	private String ebpCode;
	private String payCode;
	private String payTransactionId;
	private double totalAmount;
	private String currency;
	private String verDept;
	private String payType;
	private String tradingTime;
	private String note;
	
	public void encode(){
		try {
			initalRequest = URLEncoder.encode(initalRequest, "utf-8");
			initalResponse = URLEncoder.encode(initalResponse,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getInitalRequest() {
		return initalRequest;
	}
	public void setInitalRequest(String initalRequest) {
		this.initalRequest = initalRequest;
	}
	public String getInitalResponse() {
		return initalResponse;
	}
	public void setInitalResponse(String initalResponse) {
		this.initalResponse = initalResponse;
	}
	public String getEbpCode() {
		return ebpCode;
	}
	public void setEbpCode(String ebpCode) {
		this.ebpCode = ebpCode;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPayTransactionId() {
		return payTransactionId;
	}
	public void setPayTransactionId(String payTransactionId) {
		this.payTransactionId = payTransactionId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getVerDept() {
		return verDept;
	}
	public void setVerDept(String verDept) {
		this.verDept = verDept;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTradingTime() {
		return tradingTime;
	}
	public void setTradingTime(String tradingTime) {
		this.tradingTime = tradingTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
