package com.zm.goods.activity.model.bargain.bo;

import java.util.ArrayList;
import java.util.List;

import com.zm.goods.activity.model.bargain.base.IUserBargain;

public class UserBargainEntity implements IUserBargain{

	private int id;
	private String itemId;
	private boolean start;// 砍价是否结束
	private String createTime;// 开团时间
	private double initPrice;
	private double floorPrice;
	private List<BargainRecord> recordList;
	private int userId;
	private String userName;
	private String userImg;

	public final void setBargainRecord(BargainRecord record) {
		if (recordList == null) {
			recordList = new ArrayList<BargainRecord>();
		}
		recordList.add(record);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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

	public List<BargainRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BargainRecord> recordList) {
		this.recordList = recordList;
	}

}
