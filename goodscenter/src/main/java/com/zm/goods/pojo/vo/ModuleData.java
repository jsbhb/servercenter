package com.zm.goods.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.zm.goods.pojo.ActivityData;
import com.zm.goods.pojo.DictData;

@JsonInclude(Include.NON_NULL)
public class ModuleData {

	private String title;
	
	private String href;
	
	private String picPath;
	
	private String goodsId;
	
	private String specs;
	
	private Double price;
	
	private String origin;
	
	private String description;

	public ModuleData(DictData data){
		this.title = data.getTitle();
		this.description = data.getDescription();
		this.href = data.getHref();
		this.picPath = data.getPicPath();
		this.price = data.getPrice();
		this.specs = data.getSpecs();
		this.goodsId = data.getGoodsId();
		this.origin = data.getOrigin();
	}
	
	public ModuleData(ActivityData data){
		this.title = data.getTitle();
		this.href = data.getHref();
		this.picPath = data.getPicPath();
		this.goodsId = data.getGoodsId();
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
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

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
