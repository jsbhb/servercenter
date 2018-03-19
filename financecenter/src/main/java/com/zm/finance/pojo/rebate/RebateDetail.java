package com.zm.finance.pojo.rebate;

public class RebateDetail {

	private Integer id;
	private String orderId;
	private Integer centerId;
	private Double centerRebateMoney;
	private Integer shopId;
	private Double shopRebateMoney;
	private Integer userId;
	private Double userRebateMoney;
	private String createTime;
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
	public Integer getCenterId() {
		return centerId;
	}
	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}
	public Double getCenterRebateMoney() {
		return centerRebateMoney;
	}
	public void setCenterRebateMoney(Double centerRebateMoney) {
		this.centerRebateMoney = centerRebateMoney;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Double getShopRebateMoney() {
		return shopRebateMoney;
	}
	public void setShopRebateMoney(Double shopRebateMoney) {
		this.shopRebateMoney = shopRebateMoney;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getUserRebateMoney() {
		return userRebateMoney;
	}
	public void setUserRebateMoney(Double userRebateMoney) {
		this.userRebateMoney = userRebateMoney;
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
	private String opt;
}
