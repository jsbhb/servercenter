package com.zm.pay.pojo;

import java.io.Serializable;

public class CustomConfig implements Serializable{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer supplierId;
	private String merchantCustomsCode;//海关备案号
	private String merchantCustomsName;//海关备案名称
	private String aliCustomsPlace;//支付宝海关编号
	private String wxCustomsPlace;//微信海关编号
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
	public String getAliCustomsPlace() {
		return aliCustomsPlace;
	}
	public void setAliCustomsPlace(String aliCustomsPlace) {
		this.aliCustomsPlace = aliCustomsPlace;
	}
	public String getWxCustomsPlace() {
		return wxCustomsPlace;
	}
	public void setWxCustomsPlace(String wxCustomsPlace) {
		this.wxCustomsPlace = wxCustomsPlace;
	}
}
