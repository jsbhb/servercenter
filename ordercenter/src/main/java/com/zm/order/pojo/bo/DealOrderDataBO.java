package com.zm.order.pojo.bo;

import java.util.List;

import com.zm.order.feignclient.model.OrderBussinessModel;

/**
 * @fun 用于接收计算订单商品的价格，库存，税率等信息的业务类
 * @author user
 *
 */
public class DealOrderDataBO {

	private List<OrderBussinessModel> modelList;
	
	private Integer supplierId;
	
	private Integer orderFlag;
	
	private Integer userId;
	
	private String couponIds;
	
	private boolean fx;
	
	private boolean vip;
	
	private Integer platformSource;
	
	private Integer gradeId;
	
	private Integer centerId;

	public List<OrderBussinessModel> getModelList() {
		return modelList;
	}

	public void setModelList(List<OrderBussinessModel> modelList) {
		this.modelList = modelList;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}

	public boolean isFx() {
		return fx;
	}

	public void setFx(boolean fx) {
		this.fx = fx;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public Integer getPlatformSource() {
		return platformSource;
	}

	public void setPlatformSource(Integer platformSource) {
		this.platformSource = platformSource;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}
}
