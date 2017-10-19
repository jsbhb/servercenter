package com.zm.goods.pojo;

/**
 * @author wqy
 * @fun 活动类
 *
 */
public class Activity {

	private Integer id;
	
	private String code;
	
	private String name;
	
	private Integer layoutId;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attr == null) ? 0 : attr.hashCode());
		result = prime * result + ((conditionPrice == null) ? 0 : conditionPrice.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((opt == null) ? 0 : opt.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((typeStatus == null) ? 0 : typeStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		if (attr == null) {
			if (other.attr != null)
				return false;
		} else if (!attr.equals(other.attr))
			return false;
		if (conditionPrice == null) {
			if (other.conditionPrice != null)
				return false;
		} else if (!conditionPrice.equals(other.conditionPrice))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (opt == null) {
			if (other.opt != null)
				return false;
		} else if (!opt.equals(other.opt))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (typeStatus == null) {
			if (other.typeStatus != null)
				return false;
		} else if (!typeStatus.equals(other.typeStatus))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", type=" + type + ", typeStatus=" + typeStatus + ", conditionPrice="
				+ conditionPrice + ", discount=" + discount + ", status=" + status + ", attr=" + attr + ", description="
				+ description + ", startTime=" + startTime + ", endTime=" + endTime + ", opt=" + opt + "]";
	}
	
}
