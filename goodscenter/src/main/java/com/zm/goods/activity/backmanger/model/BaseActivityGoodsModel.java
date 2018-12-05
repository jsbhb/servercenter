package com.zm.goods.activity.backmanger.model;

public class BaseActivityGoodsModel {

	protected Integer id;
	
	protected Integer activityId;
	
	protected String itemId;
	
	protected String goodsName;
	
	protected Double initPrice;
	
	protected Double floorPrice;
	
	protected Integer maxCount;
	
	protected Integer type;
	
	protected Integer duration;
	
	protected Integer status;
	
	protected String startTime;
	
	protected String endTime;
	
	protected String createTime;
	
	protected String updateTime;
	
	protected String opt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(Double initPrice) {
		this.initPrice = initPrice;
	}

	public Double getFloorPrice() {
		return floorPrice;
	}

	public void setFloorPrice(Double floorPrice) {
		this.floorPrice = floorPrice;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
