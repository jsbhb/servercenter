package com.zm.order.pojo;

import com.zm.order.feignclient.model.GoodsSpecs;

public class ShoppingCart {

	private Integer id;

	private Integer userId;

	private String itemId;

	private Integer quantity;

	private Integer centerId;

	private String goodsName;

	private Integer supplierId;

	private String createTime;

	private String updateTime;

	private GoodsSpecs goodsSpecs;

	private String picPath;

	public boolean check() {
		return userId != null && itemId != null && quantity != null && centerId != null && goodsName != null
				&& supplierId != null;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public GoodsSpecs getGoodsSpecs() {
		return goodsSpecs;
	}

	public void setGoodsSpecs(GoodsSpecs goodsSpecs) {
		this.goodsSpecs = goodsSpecs;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	@Override
	public String toString() {
		return "ShoppingCart [id=" + id + ", userId=" + userId + ", itemId=" + itemId + ", quantity=" + quantity
				+ ", centerId=" + centerId + ", goodsName=" + goodsName + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
