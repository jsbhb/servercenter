package com.zm.goods.seo.model;

import com.zm.goods.pojo.po.ComponentDataPO;
import com.zm.goods.pojo.po.ComponentPagePO;

public class SEODataModel {

	private String code;
	private Integer sort;
	private String area;
	private Object cont;
	private Object own;

	public SEODataModel(Object item, ComponentPagePO tem) {
		this.code = tem.getKey();
		this.sort = tem.getSort();
		this.area = tem.getArea();
		if (item != null) {
			cont = item;
		} else if(tem.getList() != null && tem.getList().size() > 0){
			tem.handleData();
			for(ComponentDataPO temModel : tem.getList()){
				if(0 == temModel.getType()){
					own = temModel;
					tem.getList().remove(temModel);
					break;
				}
			}
			cont = tem.getList();
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

	public Object getCont() {
		return cont;
	}

	public void setCont(Object cont) {
		this.cont = cont;
	}

	public Object getOwn() {
		return own;
	}

	public void setOwn(Object own) {
		this.own = own;
	}

}
