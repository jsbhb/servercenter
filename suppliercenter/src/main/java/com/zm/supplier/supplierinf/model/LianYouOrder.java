package com.zm.supplier.supplierinf.model;

import java.util.List;

public class LianYouOrder {

	private String consignee;
	
	private String order_sn;
	
	private String out_order_sn;
	
	private String realName;
	
	private String imId;
	
	private String disType = "F";
	
	private String phoneMob;
	
	private String address;
	
	private String province;
	
	private String city;
	
	private String county;
	
	private Integer shipping_id = 6;
	
	private List<OutOrderGoods> outOrderGoods;

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getOut_order_sn() {
		return out_order_sn;
	}

	public void setOut_order_sn(String out_order_sn) {
		this.out_order_sn = out_order_sn;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getDisType() {
		return disType;
	}

	public void setDisType(String disType) {
		this.disType = disType;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getShipping_id() {
		return shipping_id;
	}

	public void setShipping_id(Integer shipping_id) {
		this.shipping_id = shipping_id;
	}

	public List<OutOrderGoods> getOutOrderGoods() {
		return outOrderGoods;
	}

	public void setOutOrderGoods(List<OutOrderGoods> outOrderGoods) {
		this.outOrderGoods = outOrderGoods;
	}
	
}
