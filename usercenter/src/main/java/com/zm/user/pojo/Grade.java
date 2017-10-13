package com.zm.user.pojo;
/**
 * @fun 登级表，相当于区域中心表
 * @author user
 *
 */
public class Grade {

	private Integer id;
	
	private Integer parentId;
	
	private Integer centerId;
	
	private Integer shopId;
	
	private Integer gradeType;
	
	private String gradeName;
	
	private String personInCharge;
	
	private String phone;
	
	private String attribute;
	
	private Integer gradePersonInCharge;//负责该区域中心的人
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

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
	
	
}
