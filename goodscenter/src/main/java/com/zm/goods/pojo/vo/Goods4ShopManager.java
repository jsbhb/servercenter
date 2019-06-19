package com.zm.goods.pojo.vo;

import java.util.List;

import com.zm.goods.pojo.GoodsTagEntity;

/**
 * @fun 前端微店管理-》商品列表显示实体类
 * @author user
 *
 */
public class Goods4ShopManager {

	//商品ID
	private String goodsId;
	//供应商ID
	private Integer supplierId;
	//明细ID
	private String itemId;
	//商品名称
	private String goodsName;
	//商品类型 0：跨境 2：一般贸易
	private Integer type;
	//商品零售价
	private Double retailPrice;
	//是否包邮
	private Integer freePost;
	//是否包税
	private Integer freeTax;
	//主图路径
	private String picPath;
	//供应商商品编码
	private String itemCode;
	//货号
	private String sku;
	//规格
	private String info;
	//重量
	private Integer weight;
	//消费税
	private Double exciseTax;
	//库存
	private int stock;
	//销量
	private int saleNum;
	//返佣
	private double rebate;
	//增值税
	private Double incrementTax;
	//单位
	private String unit;
	
	private List<GoodsTagEntity> tagList;
	
	public List<GoodsTagEntity> getTagList() {
		return tagList;
	}
	public void setTagList(List<GoodsTagEntity> tagList) {
		this.tagList = tagList;
	}
	public double getRebate() {
		return rebate;
	}
	public void setRebate(double rebate) {
		this.rebate = rebate;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
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
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}
	public Double getIncrementTax() {
		return incrementTax;
	}
	public void setIncrementTax(Double incrementTax) {
		this.incrementTax = incrementTax;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
