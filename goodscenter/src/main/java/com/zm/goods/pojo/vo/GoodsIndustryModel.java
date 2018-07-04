package com.zm.goods.pojo.vo;

import java.util.List;

/**
 * @author wqy
 *
 * @date 2017年6月13日
 */
public class GoodsIndustryModel {
 
	private Integer firstId;
	
	private String id;
	
	private String industryName;
	
	private String tagPath;
	
	private List<GoodsCategoryModel> dictList;

	public String getTagPath() {
		return tagPath;
	}

	public void setTagPath(String tagPath) {
		this.tagPath = tagPath;
	}

	public Integer getFirstId() {
		return firstId;
	}

	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
