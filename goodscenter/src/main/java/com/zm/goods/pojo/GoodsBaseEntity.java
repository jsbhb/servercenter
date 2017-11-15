/**  
 * Project Name:cardmanager  
 * File Name:GoodsBaseEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 12, 201710:23:12 PM  
 *  
 */
package com.zm.goods.pojo;

import com.zm.goods.common.Pagination;

/**
 * ClassName: GoodsBaseEntity <br/>
 * Function: 基础商品实体. <br/>
 * date: Nov 12, 2017 10:23:12 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsBaseEntity extends Pagination {
	private int id;
	private String brandId;// 品牌ID
	private String goodsName;// 商品名称
	private String brand;// 商品品牌
	private String incrementTax;// 增值税
	private String tariff;// 关税
	private String unit;// 单位
	private String hscode;// HSCODE
	private String encode;// 条形码
	private String firstCatalogId;// 一级分类id
	private String secondCatalogId;// 二级分类id
	private String thirdCatalogId;// 三级分类id
	private int centerId;// 区域中心ID
	private String createTime;// 注册时间
	private String updateTime;// 更新时间
	private String opt;// 操作人

	public GoodsBaseEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(String incrementTax) {
		this.incrementTax = incrementTax;
	}

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getFirstCatalogId() {
		return firstCatalogId;
	}

	public void setFirstCatalogId(String firstCatalogId) {
		this.firstCatalogId = firstCatalogId;
	}

	public String getSecondCatalogId() {
		return secondCatalogId;
	}

	public void setSecondCatalogId(String secondCatalogId) {
		this.secondCatalogId = secondCatalogId;
	}

	public String getThirdCatalogId() {
		return thirdCatalogId;
	}

	public void setThirdCatalogId(String thirdCatalogId) {
		this.thirdCatalogId = thirdCatalogId;
	}

	public int getCenterId() {
		return centerId;
	}

	public void setCenterId(int centerId) {
		this.centerId = centerId;
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
