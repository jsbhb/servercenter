package com.zm.goods.pojo.po;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ComponentPagePO {

	private Integer id;
	private String key;
	private String area;
	private Integer sort;
	private List<ComponentDataPO> list;
	
	public void handleData(){
		if(list != null && list.size() > 0){
			for(ComponentDataPO tem : list){
				tem.handleData();
			}
		}
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public List<ComponentDataPO> getList() {
		return list;
	}
	public void setList(List<ComponentDataPO> list) {
		this.list = list;
	}
}
