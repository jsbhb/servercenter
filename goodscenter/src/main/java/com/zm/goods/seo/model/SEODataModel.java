package com.zm.goods.seo.model;

import com.zm.goods.pojo.po.ComponentPagePO;

public class SEODataModel {

	private String code;
	private Integer sort;
	private String area;
	private Object data;

	public SEODataModel(Object item, ComponentPagePO tem) {
		this.code = tem.getKey();
		this.sort = tem.getSort();
		this.area = tem.getArea();
		if (item != null) {
			data = item;
		} else if(tem.getList() != null && tem.getList().size() > 0){
			tem.handleData();
			data = tem.getList();
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
