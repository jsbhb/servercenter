package com.zm.goods.activity.backmanger.model;

public class BargainActivityShowPageRecordModel {

	private Integer id;
	private String userId;
	private String name;
	private String nickName;
	private String createTime;
	private Integer person;
	private double disCount;
	private boolean buy;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getPerson() {
		return person;
	}
	public void setPerson(Integer person) {
		this.person = person;
	}
	public double getDisCount() {
		return disCount;
	}
	public void setDisCount(double disCount) {
		this.disCount = disCount;
	}
	public boolean isBuy() {
		return buy;
	}
	public void setBuy(boolean buy) {
		this.buy = buy;
	}
}
