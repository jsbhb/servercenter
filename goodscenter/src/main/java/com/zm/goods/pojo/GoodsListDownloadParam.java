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
	private List<String> firstCatalogList;
	private List<String> secondCatalogList;
	private List<String> thirdCatalogList;
	private Integer itemStatus;
	
	
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
	public List<String> getFirstCatalogList() {
		return firstCatalogList;
	}
	public void setFirstCatalogList(List<String> firstCatalogList) {
		this.firstCatalogList = firstCatalogList;
	}
	public List<String> getSecondCatalogList() {
		return secondCatalogList;
	}
	public void setSecondCatalogList(List<String> secondCatalogList) {
		this.secondCatalogList = secondCatalogList;
	}
	public List<String> getThirdCatalogList() {
		return thirdCatalogList;
	}
	public void setThirdCatalogList(List<String> thirdCatalogList) {
		this.thirdCatalogList = thirdCatalogList;
	}
	public Integer getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}
}