package com.zm.order.pojo;

import java.util.List;

import com.zm.order.common.Pagination;

import io.swagger.annotations.ApiModel;

@ApiModel
public class OrderInfo extends Pagination{

	private Integer id;

	protected String orderId;

	// 订单拆分时提供一个总ID
	protected String combinationId;

	protected Integer userId;

	protected Integer status;// 0：初始；1：已付款;2：支付单报关;3：已发仓库；4：已报海关；5：单证放行；6：已发货；7：已收货；8：退单;9、超时取消;99异常状态

	// 物流、自提
	protected Integer expressType;// 0：快递；1：自提

	// 区域中心ID
	protected Integer centerId;

	protected Integer shopId;

	protected Integer guideId;

	protected Integer supplierId;

	protected Integer tdq;
	
	protected Integer orderSource;

	protected Integer weight;

	protected String gtime;

	private String sendTime;

	private String createTime;

	private String updateTime;

	protected String remark;

	protected Integer orderFlag;// 0:跨境；1：大贸;2：一般贸易

	private String statusArr;

	protected OrderDetail orderDetail;

	protected List<OrderGoods> orderGoodsList;

	private List<ThirdOrderInfo> orderExpressList;

	private String startTime;

	private String endTime;

	private String couponIds;
	
	private Integer pushUserId;
	
	private Integer tagFun;
	
	private Integer manual;//是否手工单，0：自动；1手动

	protected Integer createType;// 0:普通订单；1：活动订单；2：优惠券订单；3：活动+优惠券
	
	private Integer isEshopIn;//Eshop是否入库，0：未入库；1入库
	
	private String deliveryTime;

	public boolean check() {
		if (orderDetail == null || orderGoodsList == null || orderFlag == null || createType == null || userId == null
				|| tdq == null || expressType == null || supplierId == null) {
			return false;
		}
		return true;
	}

	public Integer getManual() {
		return manual;
	}

	public void setManual(Integer manual) {
		this.manual = manual;
	}

	public Integer getTagFun() {
		return tagFun;
	}

	public void setTagFun(Integer tagFun) {
		this.tagFun = tagFun;
	}

	public Integer getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}

	public Integer getPushUserId() {
		return pushUserId;
	}

	public void setPushUserId(Integer pushUserId) {
		this.pushUserId = pushUserId;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public String getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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

	public List<ThirdOrderInfo> getOrderExpressList() {
		return orderExpressList;
	}

	public void setOrderExpressList(List<ThirdOrderInfo> orderExpressList) {
		this.orderExpressList = orderExpressList;
	}

	public Integer getIsEshopIn() {
		return isEshopIn;
	}

	public void setIsEshopIn(Integer isEshopIn) {
		this.isEshopIn = isEshopIn;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
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