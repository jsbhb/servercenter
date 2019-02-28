package com.zm.goods.pojo;

import java.util.List;

import com.zm.goods.pojo.base.Pagination;


public class GuidePropertyEntity  extends Pagination {

	private int id;
	private String name;
	private List<GuidePropertyValueEntity> valueList;
	private String createTime;
	private String updateTime;
	private String opt;
	private String val;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GuidePropertyValueEntity> getValueList() {
		return valueList;
	}
	public void setValueList(List<GuidePropertyValueEntity> valueList) {
		this.valueList = valueList;
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
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
}
