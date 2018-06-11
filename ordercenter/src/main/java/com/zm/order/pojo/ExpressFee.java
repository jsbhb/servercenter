package com.zm.order.pojo;

public class ExpressFee {

	private Integer id;
	
	private Integer templateId;
	
	private Double fee;
	
	private Integer weight;
	
	private Double heavyFee;
	
	private Integer supplierId;
	
	private String includeProvince;

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getIncludeProvince() {
		return includeProvince;
	}

	public void setIncludeProvince(String includeProvince) {
		this.includeProvince = includeProvince;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Double getHeavyFee() {
		return heavyFee;
	}

	public void setHeavyFee(Double heavyFee) {
		this.heavyFee = heavyFee;
	}
	
}
