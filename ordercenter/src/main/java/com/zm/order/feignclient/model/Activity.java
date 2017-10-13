package com.zm.order.feignclient.model;

/**
 * @author wqy
 * @fun 活动类
 *
 */
public class Activity {

	private Integer id;
	
	private Integer type;//0:限时特推；1：满多少打几折；2：满多少减多少
	
	private Integer typeStatus;//0:特定区域;1:全场
	
	private Double conditionPrice;
	
	private Double discount;
	
	private Integer status;
	
	private String attr;
	
	private String description;
	
	private String startTime;
	
	private String endTime;
	
	private String opt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTypeStatus() {
		return typeStatus;
	}

	public void setTypeStatus(Integer typeStatus) {
		this.typeStatus = typeStatus;
	}

	public Double getConditionPrice() {
		return conditionPrice;
	}

	public void setConditionPrice(Double conditionPrice) {
		this.conditionPrice = conditionPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", type=" + type + ", typeStatus=" + typeStatus + ", conditionPrice="
				+ conditionPrice + ", discount=" + discount + ", status=" + status + ", attr=" + attr + ", description="
				+ description + ", startTime=" + startTime + ", endTime=" + endTime + ", opt=" + opt + "]";
	}
	
	
}
