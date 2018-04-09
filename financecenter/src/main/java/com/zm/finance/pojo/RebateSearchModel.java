package com.zm.finance.pojo;

import java.util.List;

public class RebateSearchModel extends Pagination{

	private Integer type;
	private List<String> list;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	
	
}
