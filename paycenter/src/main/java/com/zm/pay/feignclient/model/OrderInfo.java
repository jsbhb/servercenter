package com.zm.pay.feignclient.model;

import java.util.List;

public class OrderInfo {

	private Integer id;

	private String orderId;

	// 订单拆分时提供一个总ID
	private String combinationId;

	private Integer userId;

	private Integer status;

	// 物流、自提
	private Integer expressType;

	// 区域中心ID
	private Integer centerId;

	private Integer shopId;
	
	private Integer guideId;

	private Integer supplierId;

	private Integer tdq;

	private String carrierKey;

	private String expressId;

	private String gtime;

	private String sendTime;

	private String createTime;

	private String updateTime;

	private String remark;
	
	private Integer orderFlag;
	
	private String statusArr;

	private OrderDetail orderDetail;

	private List<OrderGoods> orderGoodsList;
	
	private String startTime;
	
	private String endTime;
	
	private Integer orderSource;

	public Integer getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}

	public Integer getGuideId() {
		return guideId;
	}

	public void setGuideId(Integer guideId) {
		this.guideId = guideId;
	}

	public String getStatusArr() {
		return statusArr;
	}

	public void setStatusArr(String statusArr) {
		this.statusArr = statusArr;
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

	public Integer getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
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

	public String getCombinationId() {
		return combinationId;
	}

	public void setCombinationId(String combinationId) {
		this.combinationId = combinationId;
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

	public Integer getExpressType() {
		return expressType;
	}

	public void setExpressType(Integer expressType) {
		this.expressType = expressType;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getTdq() {
		return tdq;
	}

	public void setTdq(Integer tdq) {
		this.tdq = tdq;
	}

	public String getCarrierKey() {
		return carrierKey;
	}

	public void setCarrierKey(String carrierKey) {
		this.carrierKey = carrierKey;
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	public String getGtime() {
		return gtime;
	}

	public void setGtime(String gtime) {
		this.gtime = gtime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public List<OrderGoods> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<OrderGoods> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	@Override
	public String toString() {
		return "OrderInfo [id=" + id + ", orderId=" + orderId + ", combinationId=" + combinationId + ", userId="
				+ userId + ", status=" + status + ", expressType=" + expressType + ", centerId=" + centerId
				+ ", shopId=" + shopId + ", guideId=" + guideId + ", supplierId=" + supplierId + ", tdq=" + tdq
				+ ", carrierKey=" + carrierKey + ", expressId=" + expressId + ", gtime=" + gtime + ", sendTime="
				+ sendTime + ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark=" + remark
				+ ", orderFlag=" + orderFlag + ", statusArr=" + statusArr + ", orderDetail=" + orderDetail
				+ ", orderGoodsList=" + orderGoodsList + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}