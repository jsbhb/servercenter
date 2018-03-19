package com.zm.finance.pojo.rebate;

public class PushUserRebate {

	private Integer id;
	private Integer userId;
	private Integer shopId;
	private Double canBePresented;//可提现
	private Double alreadyPresented;//已提现
	private Double stayToAccount;//待到账
	private String remark;
	private String createTime;
	private String updateTime;
	private String opt;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getCanBePresented() {
		return canBePresented;
	}
	public void setCanBePresented(Double canBePresented) {
		this.canBePresented = canBePresented;
	}
	public Double getAlreadyPresented() {
		return alreadyPresented;
	}
	public void setAlreadyPresented(Double alreadyPresented) {
		this.alreadyPresented = alreadyPresented;
	}
	public Double getStayToAccount() {
		return stayToAccount;
	}
	public void setStayToAccount(Double stayToAccount) {
		this.stayToAccount = stayToAccount;
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	
}
