package com.zm.goods.pojo;

public class GoodsBase {

	private Integer id;
	
	private Integer brandId;
	
	private String goodsName;
	
	private String brand;
	
	private String origin;
	
	private Double taxrate;
	
	private String unit;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	@Override
	public String toString() {
		return "GoodsBase [id=" + id + ", brandId=" + brandId + ", goodsName=" + goodsName + ", brand=" + brand
				+ ", origin=" + origin + ", taxrate=" + taxrate + ", unit=" + unit + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}
	
}
