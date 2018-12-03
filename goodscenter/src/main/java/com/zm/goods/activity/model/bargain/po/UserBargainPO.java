package com.zm.goods.activity.model.bargain.po;

import java.util.List;

import com.zm.goods.activity.model.bargain.UserBargainEntity;

public class UserBargainPO extends UserBargainEntity{

	private int goodsRoleId;
	private List<BargainRecordPO> bargainList;
	private int duration;
	private String updateTime;
	private String opt;
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public List<BargainRecordPO> getBargainList() {
		return bargainList;
	}
	public void setBargainList(List<BargainRecordPO> bargainList) {
		this.bargainList = bargainList;
	}
	public int getGoodsRoleId() {
		return goodsRoleId;
	}
	public void setGoodsRoleId(int goodsRoleId) {
		this.goodsRoleId = goodsRoleId;
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
