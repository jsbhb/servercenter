package com.zm.goods.pojo;

import java.util.Set;

public class GoodsFielsMaintainBO {

	private String goodsId;
	private String goodsDetailPath;
	private Set<String> picPathList;
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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
