package com.zm.goods.pojo.vo;

import java.util.List;

/**
 * @author wqy
 *
 * @date 2017年6月13日
 */
public class GoodsIndustryModel {
 
	private Integer id;
	
	private String firstId;
	
	private String industryName;
	
	private List<GoodsCategoryModel> dictList;

	public String getFirstId() {
		return firstId;
	}

	public void setFirstId(String firstId) {
		this.firstId = firstId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public List<GoodsCategoryModel> getDictList() {
		return dictList;
	}

	public void setDictList(List<GoodsCategoryModel> dictList) {
		this.dictList = dictList;
	}

	@Override
	public String toString() {
		return "MemberIndustryModel [id=" + id + ", industryName=" + industryName + ", dictList=" + dictList + "]";
	}
	
}
