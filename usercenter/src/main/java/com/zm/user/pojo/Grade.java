package com.zm.user.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.zm.user.common.Pagination;

/**
 * @fun 登级表，相当于区域中心表
 * @author user
 *
 */
@JsonInclude(Include.NON_NULL)
public class Grade extends Pagination{

	private Integer id;

	private Integer parentId;

	private String parentGradeName;

	private Integer centerId;

	private Integer shopId;

	private Integer gradeType;
	
	private Integer gradeLevel;

	private String gradeName;

	private String personInCharge;
	
	private int personInChargeId;

	private String phone;

	private String attribute;

	private Integer gradePersonInCharge;// 负责该区域中心的人

	private String redirectUrl;//PC端域名
	
	private String mobileUrl;//手机端域名

	private String createTime;

	private String updateTime;

	private String opt;

	private String company;
	
	//新添加审核资料字段

	private String storeName;

	private String contacts;

	private String contactsPhone;

	private String province;

	private String city;

	private String district;

	private String address;

	private String storeOperator;

	private String operatorIDNum;

	private String picPath1;

	private String picPath2;

	private String picPath3;

	private String picPath4;
	
	private Integer copyMall;

	private String gradeTypeName;
	
	public Grade(){
		super();
	}

	public Integer getCopyMall() {
		return copyMall;
	}

	public void setCopyMall(Integer copyMall) {
		this.copyMall = copyMall;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public Integer getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(Integer gradeLevel) {
		this.gradeLevel = gradeLevel;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentGradeName() {
		return parentGradeName;
	}

	public void setParentGradeName(String parentGradeName) {
		this.parentGradeName = parentGradeName;
	}

	public Integer getGradeType() {
		return gradeType;
	}

	public void setGradeType(Integer gradeType) {
		this.gradeType = gradeType;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
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

	public Integer getGradePersonInCharge() {
		return gradePersonInCharge;
	}

	public void setGradePersonInCharge(Integer gradePersonInCharge) {
		this.gradePersonInCharge = gradePersonInCharge;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getPersonInChargeId() {
		return personInChargeId;
	}

	public void setPersonInChargeId(int personInChargeId) {
		this.personInChargeId = personInChargeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStoreOperator() {
		return storeOperator;
	}

	public void setStoreOperator(String storeOperator) {
		this.storeOperator = storeOperator;
	}

	public String getOperatorIDNum() {
		return operatorIDNum;
	}

	public void setOperatorIDNum(String operatorIDNum) {
		this.operatorIDNum = operatorIDNum;
	}

	public String getPicPath1() {
		return picPath1;
	}

	public void setPicPath1(String picPath1) {
		this.picPath1 = picPath1;
	}

	public String getPicPath2() {
		return picPath2;
	}

	public void setPicPath2(String picPath2) {
		this.picPath2 = picPath2;
	}

	public String getPicPath3() {
		return picPath3;
	}

	public void setPicPath3(String picPath3) {
		this.picPath3 = picPath3;
	}

	public String getPicPath4() {
		return picPath4;
	}

	public void setPicPath4(String picPath4) {
		this.picPath4 = picPath4;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getGradeTypeName() {
		return gradeTypeName;
	}

	public void setGradeTypeName(String gradeTypeName) {
		this.gradeTypeName = gradeTypeName;
	}
	
}
