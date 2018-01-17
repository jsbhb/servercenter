package com.zm.user.pojo;

public class PushUser {

	private Integer id;
	
	private Integer userId;
	
	private String phone;
	
	private String name;
	
	private Integer gradeId;
	
	private String inviter;
	
	private String specialtyChannel;
	
	private Integer status;

	public boolean check(){
		return phone != null && name != null && gradeId != null && inviter != null && specialtyChannel != null;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public String getSpecialtyChannel() {
		return specialtyChannel;
	}

	public void setSpecialtyChannel(String specialtyChannel) {
		this.specialtyChannel = specialtyChannel;
	}
	
}
