package com.zm.goods.activity.backmanger.model;

import java.util.List;

public class BargainActivityShowPageModel {

	private Integer goodsRoleId;
	private String goodsName;
	private double initPrice;
	private double floorPrice;
	private List<BargainActivityShowPageRecordModel> recordList;
	public Integer getGoodsRoleId() {
		return goodsRoleId;
	}
	public void setGoodsRoleId(Integer goodsRoleId) {
		this.goodsRoleId = goodsRoleId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public double getInitPrice() {
		return initPrice;
	}
	public void setInitPrice(double initPrice) {
		this.initPrice = initPrice;
	}
	public double getFloorPrice() {
		return floorPrice;
	}
	public void setFloorPrice(double floorPrice) {
		this.floorPrice = floorPrice;
	}
	public List<BargainActivityShowPageRecordModel> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<BargainActivityShowPageRecordModel> recordList) {
		this.recordList = recordList;
	}
}
