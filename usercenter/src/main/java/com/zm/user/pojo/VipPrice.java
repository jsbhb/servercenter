package com.zm.user.pojo;

public class VipPrice {

	private Integer id;
	
	private Integer centerId;
	
	private Integer vipLevel;
	
	private Integer duration;
	
	private Double price;
	
	private String attribute;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	@Override
	public String toString() {
		return "VipPrice [id=" + id + ", centerId=" + centerId + ", vipLevel=" + vipLevel + ", duration=" + duration
				+ ", price=" + price + ", attribute=" + attribute + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", opt=" + opt + "]";
	}
	
}
