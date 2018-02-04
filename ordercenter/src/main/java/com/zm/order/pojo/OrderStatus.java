package com.zm.order.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderStatus {

	@JsonIgnore
	private Integer id;
	private String orderId;
	private String abnormalMsg;
	private Integer status;
	private List<ExpressDetail> expressList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAbnormalMsg() {
		return abnormalMsg;
	}
	public void setAbnormalMsg(String abnormalMsg) {
		this.abnormalMsg = abnormalMsg;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<ExpressDetail> getExpressList() {
		return expressList;
	}
	public void setExpressList(List<ExpressDetail> expressList) {
		this.expressList = expressList;
	}
	
}
