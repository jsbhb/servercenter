package com.zm.order.pojo;



/**  
 * ClassName: OrderDetail <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 11, 2017 2:57:13 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class OrderDetail {

	private Integer id;
	
	private String orderId;
	
	private Integer orderFlag;
	
	private Integer payType;
	
	private String payNo;
	
	//发货地
	private String deliveryPlace;
	
	//自提地址
	private String carryAddress;
	
	private String receiveName;
	
	private String receivePhone;
	
	private String receiveProvince;
	
	private String receiveCity;
	
	private String receiveArea;
	
	private String receiveAddress;
	
	private String receiveZipCode;
	
	private String remark;

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

	public Integer getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getDeliveryPlace() {
		return deliveryPlace;
	}

	public void setDeliveryPlace(String deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}

	public String getCarryAddress() {
		return carryAddress;
	}

	public void setCarryAddress(String carryAddress) {
		this.carryAddress = carryAddress;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public String getReceiveProvince() {
		return receiveProvince;
	}

	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}

	public String getReceiveCity() {
		return receiveCity;
	}

	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}

	public String getReceiveArea() {
		return receiveArea;
	}

	public void setReceiveArea(String receiveArea) {
		this.receiveArea = receiveArea;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveZipCode() {
		return receiveZipCode;
	}

	public void setReceiveZipCode(String receiveZipCode) {
		this.receiveZipCode = receiveZipCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "OrderDetail [id=" + id + ", orderId=" + orderId + ", orderFlag=" + orderFlag + ", payType=" + payType
				+ ", payNo=" + payNo + ", deliveryPlace=" + deliveryPlace + ", carryAddress=" + carryAddress
				+ ", receiveName=" + receiveName + ", receivePhone=" + receivePhone + ", receiveProvince="
				+ receiveProvince + ", receiveCity=" + receiveCity + ", receiveArea=" + receiveArea
				+ ", receiveAddress=" + receiveAddress + ", receiveZipCode=" + receiveZipCode + ", remark=" + remark
				+ "]";
	}
	
}
