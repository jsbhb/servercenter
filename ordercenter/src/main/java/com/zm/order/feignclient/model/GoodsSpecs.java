package com.zm.order.feignclient.model;

import java.util.List;

/**  
 * ClassName: GoodsSpecs <br/>  
 * Function: 商品规格，包括价格. <br/>   
 * date: Aug 22, 2017 2:17:27 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class GoodsSpecs {

	private Integer id;
	
	private Integer goodsId;
	
	private String itemId;
	
	private String itemCode;
	
	private String sku;
	
	private Integer promotion;
	
	private String info;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;
	
	private Double minPrice;
	
	private Double maxPrice;
	
	private Integer stock;
	
	private List<GoodsPrice> priceList;
	
	private String picPath;

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getPromotion() {
		return promotion;
	}

	public void setPromotion(Integer promotion) {
		this.promotion = promotion;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public List<GoodsPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<GoodsPrice> priceList) {
		this.priceList = priceList;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Override
	public String toString() {
		return "GoodsSpecs [id=" + id + ", goodsId=" + goodsId + ", itemId=" + itemId + ", itemCode=" + itemCode
				+ ", sku=" + sku + ", promotion=" + promotion + ", info=" + info + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice
				+ ", stock=" + stock + ", priceList=" + priceList + ", picPath=" + picPath + "]";
	}
	
}
