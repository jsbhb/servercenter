package com.zm.goods.pojo;

public class GoodsPriceRatioEntity {
	private Integer id;
	private String itemId;
	private String ratioPlatformId;// 比价平台id
	private String ratioPlatformName;// 比价平台名称
	private Double ratioPrice;// 平台价格
	private Integer evaluateQty;// 评价数
	private Integer salesVolume;// 销量
	private Integer status;// 是否根据网址抓取 0：不抓取;1：抓取
	private String grabAddress;// 抓取地址
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	
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
	public String getRatioPlatformId() {
		return ratioPlatformId;
	}
	public void setRatioPlatformId(String ratioPlatformId) {
		this.ratioPlatformId = ratioPlatformId;
	}
	public String getRatioPlatformName() {
		return ratioPlatformName;
	}
	public void setRatioPlatformName(String ratioPlatformName) {
		this.ratioPlatformName = ratioPlatformName;
	}
	public Double getRatioPrice() {
		return ratioPrice;
	}
	public void setRatioPrice(Double ratioPrice) {
		this.ratioPrice = ratioPrice;
	}
	public Integer getEvaluateQty() {
		return evaluateQty;
	}
	public void setEvaluateQty(Integer evaluateQty) {
		this.evaluateQty = evaluateQty;
	}
	public Integer getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getGrabAddress() {
		return grabAddress;
	}
	public void setGrabAddress(String grabAddress) {
		this.grabAddress = grabAddress;
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
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
}
