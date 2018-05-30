package com.zm.user.pojo.po;

import com.zm.user.common.Pagination;

/**
 * @fun 微店主数据库持久化类
 * @author wqy
 *
 */
public class ShopKeeperPO extends Pagination{

	private Integer id;
	private String shopName;//店铺名称
	private String province;//省
	private String city;//市
	private String area;//区；
	private String address;//地址
	private String shopPath;//店铺地址
	private String picPath;//图片地址
	private String keeperName;//店主姓名
	private String occupation;//职业
	private Double sales;//销售额
	private String phone;
	private Integer level;
	private String attribute;//备用属性
	private String createTime;
	private String updateTime;
	private String opt;
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public Double getSales() {
		return sales;
	}
	public void setSales(Double sales) {
		this.sales = sales;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
}
