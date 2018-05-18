package com.zm.goods.pojo;

import java.util.List;

public class GoodsListDownloadParam {

	private Integer gradeType;
	private Integer supplierId;
	private List<String> itemIdList;
	private Integer proportionFlg;
	
	
	public Integer getGradeType() {
		return gradeType;
	}
	public void setGradeType(Integer gradeType) {
		this.gradeType = gradeType;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public List<String> getItemIdList() {
		return itemIdList;
	}
	public void setItemIdList(List<String> itemIdList) {
		this.itemIdList = itemIdList;
	}
	public Integer getProportionFlg() {
		return proportionFlg;
	}
	public void setProportionFlg(Integer proportionFlg) {
		this.proportionFlg = proportionFlg;
	}
	
	
}