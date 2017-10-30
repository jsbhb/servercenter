package com.zm.order.pojo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OrderInfo {

	private Integer id;

	private String orderId;

	// 订单拆分时提供一个总ID
	@ApiModelProperty(value = "不同供应商拆单时，产生的统一订单号", dataType="string")
	private String combinationId;

	@ApiModelProperty(value = "用户ID",dataType="integer")
	private Integer userId;

	private Integer status;

	@ApiModelProperty(value = "快递方式0：快递；1：自提",dataType="integer")
	// 物流、自提
	private Integer expressType;

	@ApiModelProperty(value = "该客户端ID",dataType="integer")
	// 区域中心ID
	private Integer centerId;

	@ApiModelProperty(value = "店铺ID,店铺下单必传",dataType="integer")
	private Integer shopId;
	
	@ApiModelProperty(value = "导购ID",dataType="integer")
	private Integer guideId;

	@ApiModelProperty(value = "供应商ID",dataType="integer")
	private Integer supplierId;

	@ApiModelProperty(value = "商品条数",dataType="integer")
	private Integer tdq;

	private String gtime;

	private String sendTime;

	private String createTime;

	private String updateTime;

	private String remark;
	
	@ApiModelProperty(value = "订单类型0:跨境；1：大贸;2：一般贸易",dataType="integer")
	private Integer orderFlag;
	
	private String statusArr;

	@ApiModelProperty(value = "订单详细信息",dataType="com.zm.order.pojo.OrderDetail")
	private OrderDetail orderDetail;

	@ApiModelProperty(value = "订单商品信息",dataType="java.util.List")
	private List<OrderGoods> orderGoodsList;
	
	private List<OrderExpress> orderExpressList;
	
	private String startTime;
	
	private String endTime;

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

	public List<OrderExpress> getOrderExpressList() {
		return orderExpressList;
	}

	public void setOrderExpressList(List<OrderExpress> orderExpressList) {
		this.orderExpressList = orderExpressList;
	}

	@Override
	public String toString() {
		return "OrderInfo [id=" + id + ", orderId=" + orderId + ", combinationId=" + combinationId + ", userId="
				+ userId + ", status=" + status + ", expressType=" + expressType + ", centerId=" + centerId
				+ ", shopId=" + shopId + ", guideId=" + guideId + ", supplierId=" + supplierId + ", tdq=" + tdq
				+ ", gtime=" + gtime + ", sendTime=" + sendTime + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", remark=" + remark + ", orderFlag=" + orderFlag + ", statusArr=" + statusArr
				+ ", orderDetail=" + orderDetail + ", orderGoodsList=" + orderGoodsList + ", orderExpressList="
				+ orderExpressList + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}