package com.zm.supplier.pojo;

public class OrderStatus {
	
	private String uniquId;

	private String orderId;
	
	private String thirdOrderId;
	
	private String status;
	
	private String packageweight;
	
	private String logisticsCode;
	
	private String expressId;
	
	private Integer supplierId;
	
	public String getUniquId() {
		return uniquId;
	}

	public void setUniquId(String uniquId) {
		this.uniquId = uniquId;
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	public String getPackageweight() {
		return packageweight;
	}

	public void setPackageweight(String packageweight) {
		this.packageweight = packageweight;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expressId == null) ? 0 : expressId.hashCode());
		result = prime * result + ((logisticsCode == null) ? 0 : logisticsCode.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((packageweight == null) ? 0 : packageweight.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((supplierId == null) ? 0 : supplierId.hashCode());
		result = prime * result + ((thirdOrderId == null) ? 0 : thirdOrderId.hashCode());
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
		OrderStatus other = (OrderStatus) obj;
		if (expressId == null) {
			if (other.expressId != null)
				return false;
		} else if (!expressId.equals(other.expressId))
			return false;
		if (logisticsCode == null) {
			if (other.logisticsCode != null)
				return false;
		} else if (!logisticsCode.equals(other.logisticsCode))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (packageweight == null) {
			if (other.packageweight != null)
				return false;
		} else if (!packageweight.equals(other.packageweight))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (supplierId == null) {
			if (other.supplierId != null)
				return false;
		} else if (!supplierId.equals(other.supplierId))
			return false;
		if (thirdOrderId == null) {
			if (other.thirdOrderId != null)
				return false;
		} else if (!thirdOrderId.equals(other.thirdOrderId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderStatus [orderId=" + orderId + ", thirdOrderId=" + thirdOrderId + ", status=" + status
				+ ", packageweight=" + packageweight + ", logisticsCode=" + logisticsCode + ", expressId=" + expressId
				+ ", supplierId=" + supplierId + "]";
	}

}
