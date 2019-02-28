package com.zm.goods.pojo;

import java.util.List;

import com.zm.goods.pojo.base.Pagination;


public class PropertyEntity  extends Pagination {

	private int id;
	private int type;
	private String name;
	private List<PropertyValueEntity> valueList;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PropertyValueEntity> getValueList() {
		return valueList;
	}
	public void setValueList(List<PropertyValueEntity> valueList) {
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
