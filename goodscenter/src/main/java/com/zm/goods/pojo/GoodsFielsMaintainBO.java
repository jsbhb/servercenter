package com.zm.goods.pojo;

import java.util.Set;

public class GoodsFielsMaintainBO {

	private String itemCode;
	private String goodsDetailPath;
	private Set<String> picPathList;
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getGoodsDetailPath() {
		return goodsDetailPath;
	}
	public void setGoodsDetailPath(String goodsDetailPath) {
		this.goodsDetailPath = goodsDetailPath;
	}
	public Set<String> getPicPathList() {
		return picPathList;
	}
	public void setPicPathList(Set<String> picPathList) {
		this.picPathList = picPathList;
	}
	
}
