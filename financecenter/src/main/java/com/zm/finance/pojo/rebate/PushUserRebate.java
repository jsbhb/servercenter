package com.zm.finance.pojo.rebate;

public class PushUserRebate extends Rebate{

	private Integer userId;
	private Integer shopId;
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
}
