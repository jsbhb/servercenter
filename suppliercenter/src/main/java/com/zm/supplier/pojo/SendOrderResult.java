package com.zm.supplier.pojo;

public class SendOrderResult {
	
	private String uniquId;

	private String orderId;
	
	private String thirdOrderId;
	
	private Integer supplierId;
	
	private String itemCode;
	
	private String itemName;
	
	private String itemId;
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUniquId() {
		return uniquId;
	}

	public void setUniquId(String uniquId) {
		this.uniquId = uniquId;
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

	@Override
	public String toString() {
		return "SendOrderResult [orderId=" + orderId + ", thirdOrderId=" + thirdOrderId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
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
		SendOrderResult other = (SendOrderResult) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
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
	
}
