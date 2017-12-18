package com.zm.activity.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Rule {

	@JsonIgnore
	private Integer id;
	
	private String ruleId;
	
	private String description;
	
	private Double condition;//条件
	
	private Double deductibleValue;//面额
	
	private Integer valueType;//0折扣券；1金额券
	
	private Integer range;//使用范围0全场，1一级分了；2二级分类；3三级分类；4特定商品
	
	private String category;
	
	private Integer superposition;//是否可叠加：0否，1是
	
	private Integer weight;//权重
	
	private List<CouponGoodsbinding> bindingList;

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public List<CouponGoodsbinding> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<CouponGoodsbinding> bindingList) {
		this.bindingList = bindingList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCondition() {
		return condition;
	}

	public void setCondition(Double condition) {
		this.condition = condition;
	}

	public Double getDeductibleValue() {
		return deductibleValue;
	}

	public void setDeductibleValue(Double deductibleValue) {
		this.deductibleValue = deductibleValue;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public Integer getRange() {
		return range;
	}

	public void setRange(Integer range) {
		this.range = range;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getSuperposition() {
		return superposition;
	}

	public void setSuperposition(Integer superposition) {
		this.superposition = superposition;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	
}
