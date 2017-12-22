package com.zm.order.feignclient.model;

import java.io.Serializable;
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
public class GoodsSpecs implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer goodsId;

	private String itemId;

	private String itemCode;

	private String sku;

	private Integer promotion;

	private String info;

	private Integer status;

	private String createTime;

	private String updateTime;

	private String opt;

	private Double minPrice;

	private Double maxPrice;

	private Double vipMinPrice;

	private Double vipMaxPrice;

	private Double realMinPrice;

	private Double realMaxPrice;

	private Double realVipMinPrice;

	private Double realVipMaxPrice;

	private String thirdCategory;

	private String secondCategory;

	private String firstCategory;

	private Integer stock;

	private Double incrementTax;

	private Integer weight;

	private Double exciseTax;

	private List<GoodsPrice> priceList;

	public String getThirdCategory() {
		return thirdCategory;
	}

	public void setThirdCategory(String thirdCategory) {
		this.thirdCategory = thirdCategory;
	}

	public String getSecondCategory() {
		return secondCategory;
	}

	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
	}

	public String getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}

	public Double getRealMinPrice() {
		return realMinPrice;
	}

	public void setRealMinPrice(Double realMinPrice) {
		this.realMinPrice = realMinPrice;
	}

	public Double getRealMaxPrice() {
		return realMaxPrice;
	}

	public void setRealMaxPrice(Double realMaxPrice) {
		this.realMaxPrice = realMaxPrice;
	}

	public Double getRealVipMinPrice() {
		return realVipMinPrice;
	}

	public void setRealVipMinPrice(Double realVipMinPrice) {
		this.realVipMinPrice = realVipMinPrice;
	}

	public Double getRealVipMaxPrice() {
		return realVipMaxPrice;
	}

	public void setRealVipMaxPrice(Double realVipMaxPrice) {
		this.realVipMaxPrice = realVipMaxPrice;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(Double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public Double getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(Double incrementTax) {
		this.incrementTax = incrementTax;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getVipMinPrice() {
		return vipMinPrice;
	}

	public void setVipMinPrice(Double vipMinPrice) {
		this.vipMinPrice = vipMinPrice;
	}

	public Double getVipMaxPrice() {
		return vipMaxPrice;
	}

	public void setVipMaxPrice(Double vipMaxPrice) {
		this.vipMaxPrice = vipMaxPrice;
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
				+ ", vipMinPrice=" + vipMinPrice + ", vipMaxPrice=" + vipMaxPrice + ", stock=" + stock + ", priceList="
				+ priceList + "]";
	}

}
