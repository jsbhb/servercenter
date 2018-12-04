package com.zm.goods.activity.model.bargain.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @fun 前端我的砍价实体类
 * @author user
 *
 */
public class MyBargain {
	private int id;
	private String goodsName;
	private double goodsPrice;// 原价
	private String createTime;
	private double bargainPrice;// 已砍价格
	private double lowPrice;// 底价
	private int stock;
	private String description;
	private String goodsImg;
	private int duration;
	private String userImg;
	private String originCountry;
	private List<MyBargainRecord> bargainList;
	private boolean start;
	// 只是业务中间需要用到，传到前端不需要
	@JsonIgnore
	private String itemId;
	// 只是业务中间需要用到，传到前端不需要
	@JsonIgnore
	private int userId;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public double getBargainPrice() {
		return bargainPrice;
	}

	public void setBargainPrice(double bargainPrice) {
		this.bargainPrice = bargainPrice;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public List<MyBargainRecord> getBargainList() {
		return bargainList;
	}

	public void setBargainList(List<MyBargainRecord> bargainList) {
		this.bargainList = bargainList;
	}
}
