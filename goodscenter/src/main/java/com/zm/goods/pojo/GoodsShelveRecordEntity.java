package com.zm.goods.pojo;

public class GoodsShelveRecordEntity {

	private int id;
	private String createTime;// 记录时间
	private int shelveType;// 类型，0：上架;1：下架
	private int shelveMode;// 模式，0：自动
	private int number;// 数量
	private String detail;// 明细
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getShelveType() {
		return shelveType;
	}
	public void setShelveType(int shelveType) {
		this.shelveType = shelveType;
	}
	public int getShelveMode() {
		return shelveMode;
	}
	public void setShelveMode(int shelveMode) {
		this.shelveMode = shelveMode;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
