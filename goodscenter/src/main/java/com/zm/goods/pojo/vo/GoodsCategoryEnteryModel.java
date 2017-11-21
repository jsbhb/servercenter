package com.zm.goods.pojo.vo;

/**
 * @author wqy
 * @fun 行业分类细分model
 * @date 2017年6月8日
 */
public class GoodsCategoryEnteryModel {

	private String id;
	
	private Integer thirdId;
	
	private String entryName;
	
	public Integer getThirdId() {
		return thirdId;
	}

	public void setThirdId(Integer thirdId) {
		this.thirdId = thirdId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}


	@Override
	public String toString() {
		return "MemberCategoryEnteryModel [id=" + id + ", entryName=" + entryName + "]";
	}
	
}
