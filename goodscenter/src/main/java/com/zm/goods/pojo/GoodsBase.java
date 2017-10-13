package com.zm.goods.pojo;

public class GoodsBase {

	private Integer id;
	
	private String brandId;
	
	private String goodsName;
	
	private String brand;
	
	private String origin;
	
	private Double tariff;
	
	private Double exciseTax;
	
	private Double incrementTax;
	
	private String hscode;
	
	private String encode;
	
	private String unit;
	
	private String createTime;
	
	private String updateTime;
	
	private String opt;

	public Double getTariff() {
		return tariff;
	}

	public void setTariff(Double tariff) {
		this.tariff = tariff;
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

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
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
				+ ", origin=" + origin + ", tariff=" + tariff + ", exciseTax=" + exciseTax + ", incrementTax="
				+ incrementTax + ", hscode=" + hscode + ", encode=" + encode + ", unit=" + unit + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}
	
}
