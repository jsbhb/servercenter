package com.zm.order.pojo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonInclude;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfoDTO {

	private Integer id;

	private String orderId;

	private Integer status;

	private Integer mallId;

	private Integer gradeId;

	private String createTime;

	private String updateTime;

	private String startTime;

	private String endTime;
	
	private Double payment;
	
	private String receiveName;
	
	private String receivePhone;
	
	private Integer isEshopIn;

	private List<OrderGoodsDTO> orderGoodsList;
	
	private String opt;
	
	private Integer tdq;
	
	private Integer payType;
	
	private Double actualPayment;
	
	private Double grossProfit;
	
	private Integer searchDateType;
	

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMallId() {
		return mallId;
	}

	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public Integer getIsEshopIn() {
		return isEshopIn;
	}

	public void setIsEshopIn(Integer isEshopIn) {
		this.isEshopIn = isEshopIn;
	}

	public List<OrderGoodsDTO> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<OrderGoodsDTO> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public Integer getTdq() {
		return tdq;
	}

	public void setTdq(Integer tdq) {
		this.tdq = tdq;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Double getActualPayment() {
		return actualPayment;
	}

	public void setActualPayment(Double actualPayment) {
		this.actualPayment = actualPayment;
	}

	public Double getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public Integer getSearchDateType() {
		return searchDateType;
	}

	public void setSearchDateType(Integer searchDateType) {
		this.searchDateType = searchDateType;
	}
}