package com.zm.finance.pojo.capitalpool;

public class CapitalPoolDetail {

	private Integer id;
	private Integer centerId;
	private Integer payType;
	private Integer bussinessType;
	private Double money;
	private String payNo;
	private String orderId;
	private String remark;
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
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getBussinessType() {
		return bussinessType;
	}
	public void setBussinessType(Integer bussinessType) {
		this.bussinessType = bussinessType;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
		return "CapitalPoolDetail [id=" + id + ", centerId=" + centerId + ", payType=" + payType + ", bussinessType="
				+ bussinessType + ", money=" + money + ", payNo=" + payNo + ", orderId=" + orderId + ", remark="
				+ remark + ", createTime=" + createTime + ", opt=" + opt + "]";
	}
	
}
