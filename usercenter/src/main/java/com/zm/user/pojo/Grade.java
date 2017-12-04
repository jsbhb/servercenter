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

	private String gradeName;

	private String personInCharge;
	
	private int personInChargeId;

	private String phone;

	private String attribute;

	private Integer gradePersonInCharge;// 负责该区域中心的人

	private String createTime;

	private String updateTime;

	private String opt;

	private String company;
	
	public Grade(){
		super();
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
}
