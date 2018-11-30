package com.zm.goods.activity.model.bargain;

public class BargainRecord {

	private int userId;//砍价user
	private double bargainPrice;//砍价金额
	private boolean buy;//接龙模式下是否已经购买
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
}
