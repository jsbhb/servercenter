package com.zm.goods.activity.model.bargain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @fun 前端砍价商品
 * @author user
 *
 */
public class BargainGoods {

	private int goodsRoleId;
	private String goodsName;
	private String originCountry;
	private String description;
	private double goodsPrice;
	private String goodsImg;
	private int bargainCount;//有几人开团
	private int stock;
	@JsonIgnore
	private String itemId;//业务转换需要，前端不需要
	public int getGoodsRoleId() {
		return goodsRoleId;
	}
	public void setGoodsRoleId(int goodsRoleId) {
		this.goodsRoleId = goodsRoleId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getBargainCount() {
		return bargainCount;
	}
	public void setBargainCount(int bargainCount) {
		this.bargainCount = bargainCount;
	}
}
