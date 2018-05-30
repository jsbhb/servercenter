package com.zm.user.pojo.po;

import com.zm.user.common.Pagination;

/**
 * @fun 合伙人数据库对象
 * @author wqy
 *
 */
public class PartnerPO extends Pagination{

	private Integer id;
	private String partnerName;//企业名称
	private String province;//省
	private String city;//市
	private String area;//区；
	private String address;//地址
	private String aboutusPath;//介绍地址
	private String picPath;//图片地址
	private String personInCharge;//负责人姓名
	private String phone;
	private String attribute;//备用属性
	private String createTime;
	private String updateTime;
	private String opt;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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
	public String getAboutusPath() {
		return aboutusPath;
	}
	public void setAboutusPath(String aboutusPath) {
		this.aboutusPath = aboutusPath;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
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
}
