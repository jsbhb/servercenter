package com.zm.goods.pojo;

import java.util.List;

public class GoodsFielsMaintainBO {

	private String itemCode;
	private String goodsDetailPath;
	private List<String> picPathList;
	
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
	public List<String> getPicPathList() {
		return picPathList;
	}
	public void setPicPathList(List<String> picPathList) {
		this.picPathList = picPathList;
	}
	
}
