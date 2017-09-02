package com.zm.pay.feignclient.model;

public class UserVip {

	private Integer id;
	
	private Integer userId;
	
	private Integer centerId;
	
	private Integer duration;
	
	private Integer vipLevel;
	
	private String createTime;
	
	private Integer status;
	
	private String updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "UserVip [id=" + id + ", userId=" + userId + ", centerId=" + centerId + ", duration=" + duration
				+ ", vipLevel=" + vipLevel + ", createTime=" + createTime + ", status=" + status + ", updateTime="
				+ updateTime + "]";
	}
	
}
