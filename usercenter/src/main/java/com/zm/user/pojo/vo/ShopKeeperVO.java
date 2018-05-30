package com.zm.user.pojo.vo;

import com.zm.user.pojo.po.ShopKeeperPO;

public class ShopKeeperVO {

	private String shopName;// 店铺名称
	private String province;// 省
	private String city;// 市
	private String area;// 区；
	private String address;// 地址
	private String shopPath;// 店铺地址
	private String picPath;// 图片地址

	public ShopKeeperVO(ShopKeeperPO po) {
		this.shopName = po.getShopName();
		this.province = po.getProvince();
		this.city = po.getCity();
		this.area = po.getArea();
		this.address = po.getAddress();
		this.shopPath = po.getShopPath();
		this.picPath = po.getPicPath();
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public String getShopPath() {
		return shopPath;
	}

	public void setShopPath(String shopPath) {
		this.shopPath = shopPath;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
