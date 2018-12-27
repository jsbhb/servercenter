package com.zm.goods.activity.model.bargain.po;

public class BargainRulePO {

	private int id;
	private int activityId;
	private String startTime;
	private String endTime;
	private String createTime;
	private String updateTime;
	private String opt;
	private String itemId;// 商品itemId
	private double initPrice;// 初始价格
	private double floorPrice;// 底价
	private int minRatio;// 每砍一刀最小比例
	private int maxRatio;// 每砍一刀最大比例
	private int maxCount;// 该商品最多被砍几刀，0：无限
	private int firstMinRatio;// 第一刀最小比例
	private int firstMaxRatio;// 第一刀最大比例
	private int type;// 1:普通，2：接龙
	private int duration;// 持续时间（小时）
	private double lessMinPrice;// 砍价时，现价和底价小于该值时直接砍到底价
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public int getMinRatio() {
		return minRatio;
	}

	public void setMinRatio(int minRatio) {
		this.minRatio = minRatio;
	}

	public int getMaxRatio() {
		return maxRatio;
	}

	public void setMaxRatio(int maxRatio) {
		this.maxRatio = maxRatio;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getFirstMinRatio() {
		return firstMinRatio;
	}

	public void setFirstMinRatio(int firstMinRatio) {
		this.firstMinRatio = firstMinRatio;
	}

	public int getFirstMaxRatio() {
		return firstMaxRatio;
	}

	public void setFirstMaxRatio(int firstMaxRatio) {
		this.firstMaxRatio = firstMaxRatio;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getLessMinPrice() {
		return lessMinPrice;
	}

	public void setLessMinPrice(double lessMinPrice) {
		this.lessMinPrice = lessMinPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
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
