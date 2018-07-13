package com.zm.goods.pojo;

import java.util.List;

public class GoodsListDownloadParam {

	private Integer gradeType;
	private Integer supplierId;
	private List<String> itemIdList;
	private Integer proportionFlg;
	private List<String> tagIdList;
	private Double rebateStart;
	private Double rebateEnd;
	
	
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
	public List<String> getTagIdList() {
		return tagIdList;
	}
	public void setTagIdList(List<String> tagIdList) {
		this.tagIdList = tagIdList;
	}
	public Double getRebateStart() {
		return rebateStart;
	}
	public void setRebateStart(Double rebateStart) {
		this.rebateStart = rebateStart;
	}
	public Double getRebateEnd() {
		return rebateEnd;
	}
	public void setRebateEnd(Double rebateEnd) {
		this.rebateEnd = rebateEnd;
	}
}