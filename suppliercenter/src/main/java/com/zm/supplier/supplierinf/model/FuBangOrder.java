package com.zm.supplier.supplierinf.model;

import java.util.List;

public class FuBangOrder {

	private String order_no;
	
	private String name;
	
	private String id_num;
	
	private String logistics_name;
	
	private String consignee;
	
	private String consignee_mobile;
	
	private String province;
	
	private String city;
	
	private String district;
	
	private String consignee_addr;
	
	private List<FuBangOrderGoods> goods;

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public String getLogistics_name() {
		return logistics_name;
	}

	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsignee_mobile() {
		return consignee_mobile;
	}

	public void setConsignee_mobile(String consignee_mobile) {
		this.consignee_mobile = consignee_mobile;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getConsignee_addr() {
		return consignee_addr;
	}

	public void setConsignee_addr(String consignee_addr) {
		this.consignee_addr = consignee_addr;
	}

	public List<FuBangOrderGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<FuBangOrderGoods> goods) {
		this.goods = goods;
	}
	
	
}
