package com.zm.user.pojo;

public class VipOrder {

	private Integer id;

	private String orderId;

	private Integer vipPriceId;

	private Integer userId;

	private Integer status;

	private String createTime;

	private String updateTime;

	public boolean checkAttr() {
		return userId != null && vipPriceId != null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public Integer getVipPriceId() {
		return vipPriceId;
	}

	public void setVipPriceId(Integer vipPriceId) {
		this.vipPriceId = vipPriceId;
	}

	@Override
	public String toString() {
		return "VipOrder [id=" + id + ", orderId=" + orderId + ", vipPriceId=" + vipPriceId + ", userId=" + userId
				+ ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
