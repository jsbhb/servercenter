package com.zm.goods.activity.model.bargain.po;

import com.zm.goods.activity.model.bargain.BargainRecord;

public class BargainRecordPO extends BargainRecord{

	private int id;
	private int goodsRecordId;
	private String updateTime;
	private String opt;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodsRecordId() {
		return goodsRecordId;
	}
	public void setGoodsRecordId(int goodsRecordId) {
		this.goodsRecordId = goodsRecordId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
}
