package com.zm.goods.activity.model.bargain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * @fun 前端用户砍价记录
 * @author user
 *
 */
@JsonInclude(Include.NON_NULL)
public class MyBargainRecord {

	private int id;
	private String userName;
	private String userImg;
	private double bargainPrice;
	private boolean buy;
	//只是业务中间需要用到，传到前端不需要
	@JsonIgnore
	private int userId;
	public boolean isBuy() {
		return buy;
	}
	public void setBuy(boolean buy) {
		this.buy = buy;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public double getBargainPrice() {
		return bargainPrice;
	}
	public void setBargainPrice(double bargainPrice) {
		this.bargainPrice = bargainPrice;
	}
}
