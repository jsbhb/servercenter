package com.zm.order.pojo;

import com.zm.order.common.Pagination;

public class PurchaseOrderInfo extends Pagination{
	// 区域中心ID
	private Integer centerId;

	private Integer shopId;

	private String orderCount;

	private String orderAmountCount;

	private String startTime;

	private String endTime;
	
	private String orderId;
	
	private String goodsId;
	
	private String GoodsName;

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

	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getOrderAmountCount() {
		return orderAmountCount;
	}

	public void setOrderAmountCount(String orderAmountCount) {
		this.orderAmountCount = orderAmountCount;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return GoodsName;
	}

	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	
	
}