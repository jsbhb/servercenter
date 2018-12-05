package com.zm.goods.activity.model.bargain.po;

import java.util.List;

public class UserBargainPO{

	private int goodsRoleId;
	private List<BargainRecordPO> bargainList;
	private int duration;
	private String updateTime;
	private String opt;
	private int id;
	private String itemId;
	private boolean start;// 砍价是否结束
	private String createTime;// 开团时间
	private double initPrice;
	private double floorPrice;
	private int userId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
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
