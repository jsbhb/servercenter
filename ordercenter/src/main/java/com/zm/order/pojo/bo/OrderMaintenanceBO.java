package com.zm.order.pojo.bo;

import java.util.List;

public class OrderMaintenanceBO {

	private String orderId;
	private List<ExpressMaintenanceBO> expressList;
	private Integer status;
	private Integer supplierId;
	
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
	public List<ExpressMaintenanceBO> getExpressList() {
		return expressList;
	}
	public void setExpressList(List<ExpressMaintenanceBO> expressList) {
		this.expressList = expressList;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
