package com.zm.goods.pojo.vo;

public class TimeLimitActiveData {

	private Integer id;
	
	private String goodsId;
	
	private String goodsName;
	
	private Double price;
	
	private Double realPrice;
	
	private String picPath;
	
	private Double discount;

	public Double getPrice() {
		return price;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	
}
