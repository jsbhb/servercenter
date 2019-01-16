package com.zm.order.pojo;

import io.swagger.annotations.ApiModel;

/**
 * ClassName: OrderDetail <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 11, 2017 2:57:13 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@ApiModel(value = "orderDetail", description = "订单详细信息")
public class OrderDetail {

	private Integer id;

	private String orderId;

	private Integer payType;

	private Double payment;

	private String payTime;

	private Double postFee;

	private Double taxFee;

	private Double tariffTax;// 关税

	private Double incrementTax;// 增值税

	private Double exciseTax;// 消费税

	private String payNo;

	private Double disAmount;

	private String returnPayNo;

	private Integer customStatus;

	// 发货地
	private String deliveryPlace;

	// 自提地址
	private String carryAddress;

	private String receiveName;

	private String receivePhone;

	private String receiveProvince;

	private String receiveCity;

	private String receiveArea;

	private String receiveAddress;

	private String receiveZipCode;

	private String remark;
	
	private Double rebateFee;
	
	private String customerIdNum;
	
	private String customerName;
	
	private String customerPhone;
	
	public boolean validate() {
		return (payType != null && payment != null && postFee != null && taxFee != null
				&& tariffTax != null && incrementTax != null && exciseTax != null && disAmount != null
				&& receiveName != null && receivePhone != null && receiveProvince != null && receiveCity != null
				&& receiveArea != null && receiveAddress != null);
	}

	public String getCustomerIdNum() {
		return customerIdNum;
	}

	public void setCustomerIdNum(String customerIdNum) {
		this.customerIdNum = customerIdNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public Double getRebateFee() {
		return rebateFee;
	}

	public void setRebateFee(Double rebateFee) {
		this.rebateFee = rebateFee;
	}

	public Double getDisAmount() {
		return disAmount;
	}

	public void setDisAmount(Double disAmount) {
		this.disAmount = disAmount;
	}

	public Double getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}

	public Double getTariffTax() {
		return tariffTax;
	}

	public void setTariffTax(Double tariffTax) {
		this.tariffTax = tariffTax;
	}

	public Double getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(Double incrementTax) {
		this.incrementTax = incrementTax;
	}

	public Double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(Double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public String getReturnPayNo() {
		return returnPayNo;
	}

	public void setReturnPayNo(String returnPayNo) {
		this.returnPayNo = returnPayNo;
	}

	public Integer getCustomStatus() {
		return customStatus;
	}

	public void setCustomStatus(Integer customStatus) {
		this.customStatus = customStatus;
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

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Double getPostFee() {
		return postFee;
	}

	public void setPostFee(Double postFee) {
		this.postFee = postFee;
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
		return "OrderDetail [id=" + id + ", orderId=" + orderId + ", payType=" + payType + ", payment=" + payment
				+ ", payTime=" + payTime + ", postFee=" + postFee + ", taxFee=" + taxFee + ", payNo=" + payNo
				+ ", deliveryPlace=" + deliveryPlace + ", carryAddress=" + carryAddress + ", receiveName=" + receiveName
				+ ", receivePhone=" + receivePhone + ", receiveProvince=" + receiveProvince + ", receiveCity="
				+ receiveCity + ", receiveArea=" + receiveArea + ", receiveAddress=" + receiveAddress
				+ ", receiveZipCode=" + receiveZipCode + ", remark=" + remark + "]";
	}

}
