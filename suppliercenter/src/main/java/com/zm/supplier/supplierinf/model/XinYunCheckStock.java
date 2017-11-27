package com.zm.supplier.supplierinf.model;

public class XinYunCheckStock {

	private String opcode="get_goods_stock";
	
	private String merchant_id ;
	
	private String sku_list;
	
	private String sign;
	
	private String sign_type="MD5";

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getSku_list() {
		return sku_list;
	}

	public void setSku_list(String sku_list) {
		this.sku_list = sku_list;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
}
