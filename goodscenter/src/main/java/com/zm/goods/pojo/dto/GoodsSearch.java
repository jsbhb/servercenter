package com.zm.goods.pojo.dto;

import com.zm.goods.annotation.SearchCondition;

public class GoodsSearch {

	private String goodsId;

	private Integer centerId;

	@SearchCondition(value = SearchCondition.FILTER)
	private String specs;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String goodsName;

	@SearchCondition(value = SearchCondition.FILTER)
	private String brand;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String upShelves;

	private Double price;

	@SearchCondition(value = SearchCondition.FILTER)
	private String createTime;

	@SearchCondition(value = SearchCondition.FILTER)
	private String origin;

	private Integer status;

	private Integer popular;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String thirdCategory;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String secondCategory;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String firstCategory;

	@SearchCondition(value = SearchCondition.FILTER)
	private String priceMin;

	@SearchCondition(value = SearchCondition.FILTER)
	private String priceMax;

	@SearchCondition(value = SearchCondition.FILTER)
	private Integer type;

	@SearchCondition(value = SearchCondition.FILTER)
	private String tag;

	private Integer ratio;

	public Integer getRatio() {
		return ratio;
	}

	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(String priceMin) {
		this.priceMin = priceMin;
	}

	public String getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}

	public String getUpShelves() {
		return upShelves;
	}

	public void setUpShelves(String upShelves) {
		this.upShelves = upShelves;
	}

	public Integer getPopular() {
		return popular;
	}

	public void setPopular(Integer popular) {
		this.popular = popular;
	}

	public String getThirdCategory() {
		return thirdCategory;
	}

	public void setThirdCategory(String thirdCategory) {
		this.thirdCategory = thirdCategory;
	}

	public String getSecondCategory() {
		return secondCategory;
	}

	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
	}

	public String getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "GoodsSearch [goodsId=" + goodsId + ", price=" + price + ", priceMin=" + priceMin + ", priceMax="
				+ priceMax + ", type=" + type + ", tag=" + tag + "]";
	}

}
