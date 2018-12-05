package com.zm.goods.activity.model.bargain.po;

public class BargainRecordPO{

	private int id;
	private int goodsRecordId;
	private String updateTime;
	private String opt;
	private int userId;//砍价user
	private double bargainPrice;//砍价金额
	private boolean buy;//接龙模式下是否已经购买
	private String createTime;//砍价时间

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getBargainPrice() {
		return bargainPrice;
	}
	public void setBargainPrice(double bargainPrice) {
		this.bargainPrice = bargainPrice;
	}
	public boolean isBuy() {
		return buy;
	}
	public void setBuy(boolean buy) {
		this.buy = buy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodsRecordId() {
		return goodsRecordId;
	}
	public void setGoodsRecordId(int goodsRecordId) {
		this.goodsRecordId = goodsRecordId;
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
