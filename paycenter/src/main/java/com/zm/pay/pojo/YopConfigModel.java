package com.zm.pay.pojo;

import java.io.Serializable;
/**
 * 支付宝支付 除了退款不支持MD5用RSA，其他用MD5
 * @author user
 *
 */
public class YopConfigModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer centerId;
	//父商编
	private String parentMerchantNo;
	//子商编
	private String merchantNo;
	
	private String url;

	public String getParentMerchantNo() {
		return parentMerchantNo;
	}

	public void setParentMerchantNo(String parentMerchantNo) {
		this.parentMerchantNo = parentMerchantNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

}
