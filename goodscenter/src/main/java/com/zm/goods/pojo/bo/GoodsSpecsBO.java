package com.zm.goods.pojo.bo;
/**
 * @fun 商品规格业务实体类
 * @author user
 *
 */
public class GoodsSpecsBO {
	
	private String specsName;
	private Integer specsNameId;
	private String specsValue;
	private Integer specsValueId;
	
	public Integer getSpecsNameId() {
		return specsNameId;
	}
	public void setSpecsNameId(Integer specsNameId) {
		this.specsNameId = specsNameId;
	}
	public Integer getSpecsValueId() {
		return specsValueId;
	}
	public void setSpecsValueId(Integer specsValueId) {
		this.specsValueId = specsValueId;
	}
	public String getSpecsName() {
		return specsName;
	}
	public void setSpecsName(String specsName) {
		this.specsName = specsName;
	}
	public String getSpecsValue() {
		return specsValue;
	}
	public void setSpecsValue(String specsValue) {
		this.specsValue = specsValue;
	}
	
}
