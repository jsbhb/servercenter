package com.zm.user.pojo;

import com.zm.user.common.Pagination;

public class InviterEntity extends Pagination {

	private int id;
	private int gradeId;
	private String name;
	private String phone;
	private String invitationCode;
	private int status;
	private int userCenterId;
	private String createTime;
	private String updateTime;
	private String opt;
	private String bindName;
	private String bindPhone;
	private String ids;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUserCenterId() {
		return userCenterId;
	}
	public void setUserCenterId(int userCenterId) {
		this.userCenterId = userCenterId;
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
	public String getBindName() {
		return bindName;
	}
	public void setBindName(String bindName) {
		this.bindName = bindName;
	}
	public String getBindPhone() {
		return bindPhone;
	}
	public void setBindPhone(String bindPhone) {
		this.bindPhone = bindPhone;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
