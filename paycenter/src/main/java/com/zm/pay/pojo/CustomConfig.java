package com.zm.pay.pojo;

public class CustomConfig {

	private Integer id;
	private Integer supplierId;
	private String merchantCustomsCode;//海关备案号
	private String merchantCustomsName;//海关备案名称
	private String customsPlace;//海关编号
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getMerchantCustomsCode() {
		return merchantCustomsCode;
	}
	public void setMerchantCustomsCode(String merchantCustomsCode) {
		this.merchantCustomsCode = merchantCustomsCode;
	}
	public String getMerchantCustomsName() {
		return merchantCustomsName;
	}
	public void setMerchantCustomsName(String merchantCustomsName) {
		this.merchantCustomsName = merchantCustomsName;
	}
	public String getCustomsPlace() {
		return customsPlace;
	}
	public void setCustomsPlace(String customsPlace) {
		this.customsPlace = customsPlace;
	}
	
}
