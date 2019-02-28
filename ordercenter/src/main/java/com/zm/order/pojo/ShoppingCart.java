package com.zm.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;

@ApiModel
public class ShoppingCart {

	private Integer id;

	private Integer userId;

	private String specsTpId;

	private Integer quantity;

	private Integer gradeId;

	private String goodsName;

	private Integer supplierId;

	private String supplierName;

	@JsonIgnore
	private String createTime;
	@JsonIgnore
	private String updateTime;

	private Integer type;

	private String picPath;

	private Integer freePost;

	private Integer freeTax;

	private String href;
	
	private int platformSource;

	public int getPlatformSource() {
		return platformSource;
	}

	public void setPlatformSource(int platformSource) {
		this.platformSource = platformSource;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getFreePost() {
		return freePost;
	}

	public void setFreePost(Integer freePost) {
		this.freePost = freePost;
	}

	public Integer getFreeTax() {
		return freeTax;
	}

	public void setFreeTax(Integer freeTax) {
		this.freeTax = freeTax;
	}

	public boolean check() {
		return userId != null && specsTpId != null && quantity != null && gradeId != null && goodsName != null
				&& supplierId != null;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
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

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
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
}
