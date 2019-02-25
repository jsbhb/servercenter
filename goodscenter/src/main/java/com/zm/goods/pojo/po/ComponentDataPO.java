package com.zm.goods.pojo.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ComponentDataPO {

	private Integer id;
	private Integer cpId;
	private Integer sort;
	private String title;
	private String href;
	private String description;
	private String picPath;
	private String enname;
	private String tagPath;
	private String origin;
	private Double price;
	private String specs;
	private Integer goodsType;
	private Integer freePost;
	private Integer freeTax;
	private Integer promotion;
	private String goodsId;
	private Integer popular;
	private Integer type;

	public void handleData(){
		this.id = null;
		this.sort = null;
		this.freePost = this.freePost == 0 ? null : this.freePost;
		this.freeTax = this.freeTax == 0 ? null : this.freeTax;
		this.promotion = this.promotion == 0 ? null : this.promotion;
		this.popular = this.popular == 0 ? null : this.popular;
	}

	public Integer getCpId() {
		return cpId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getTagPath() {
		return tagPath;
	}

	public void setTagPath(String tagPath) {
		this.tagPath = tagPath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public Integer getFreePost() {
		return freePost;
	}

	public void setFreePost(Integer freePost) {
		this.freePost = freePost;
	}

	public Integer getFreeTax() {
		return freeTax;
	}

	public void setFreeTax(Integer freeTax) {
		this.freeTax = freeTax;
	}

	public Integer getPromotion() {
		return promotion;
	}

	public void setPromotion(Integer promotion) {
		this.promotion = promotion;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getPopular() {
		return popular;
	}

	public void setPopular(Integer popular) {
		this.popular = popular;
	}
}
