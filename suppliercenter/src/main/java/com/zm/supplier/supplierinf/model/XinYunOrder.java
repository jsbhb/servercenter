package com.zm.supplier.supplierinf.model;

import java.util.List;

public class XinYunOrder {

	private String opcode;
	
	private String merchant_order_no;
	
	private String pay_type = "1";//默认信用付款
	
	private String merchant_id;
	
	private String accept_name;
	
	private String card_id;
	
	private String post_code;
	
	private String telphone;
	
	private String mobile;
	
	private String province;
	
	private String city;
	
	private String area;
	
	private String address;
	
	private String card_url_front;
	
	private String card_url_back;
	
	private String message;
	
	private List<XinYunGoods> items; 
	
	private String sign;
	
	private String sign_type = "MD5";

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public String getMerchant_order_no() {
		return merchant_order_no;
	}

	public void setMerchant_order_no(String merchant_order_no) {
		this.merchant_order_no = merchant_order_no;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getAccept_name() {
		return accept_name;
	}

	public void setAccept_name(String accept_name) {
		this.accept_name = accept_name;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCard_url_front() {
		return card_url_front;
	}

	public void setCard_url_front(String card_url_front) {
		this.card_url_front = card_url_front;
	}

	public String getCard_url_back() {
		return card_url_back;
	}

	public void setCard_url_back(String card_url_back) {
		this.card_url_back = card_url_back;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<XinYunGoods> getItems() {
		return items;
	}

	public void setItems(List<XinYunGoods> items) {
		this.items = items;
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
