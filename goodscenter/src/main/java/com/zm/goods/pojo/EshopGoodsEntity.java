/**  
 * Project Name:cardmanager  
 * File Name:GoodsEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 12, 201710:28:15 PM  
 *  
 */
package com.zm.goods.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ClassName: EshopGoodsEntity <br/>
 * Function: 商品实体 <br/>
 * date: Nov 12, 2017 10:28:15 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EshopGoodsEntity{
	private Integer id;
	private Integer mallId;
	private Integer gradeId;
	private String goodsName;
	private String origin;
	private String firstCategory;
	private String brand;
	private String status;
	private String itemImg;
	private String itemId;
	private String encode;
	private Double proxyPrice;
	private Double retailPrice;
	private String remark;
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	private String goodsId;
	private Integer itemQuantity;
	private List<EshopGoodsStockEntity> goodsStockList;
	private String loc;
	private List<String> itemIdList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMallId() {
		return mallId;
	}
	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}
	public Integer getGradeId() {
		return gradeId;
	}
	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getFirstCategory() {
		return firstCategory;
	}
	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getItemImg() {
		return itemImg;
	}
	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public Double getProxyPrice() {
		return proxyPrice;
	}
	public void setProxyPrice(Double proxyPrice) {
		this.proxyPrice = proxyPrice;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getItemQuantity() {
		return itemQuantity;
	}
	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
	public List<EshopGoodsStockEntity> getGoodsStockList() {
		return goodsStockList;
	}
	public void setGoodsStockList(List<EshopGoodsStockEntity> goodsStockList) {
		this.goodsStockList = goodsStockList;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public List<String> getItemIdList() {
		return itemIdList;
	}
	public void setItemIdList(List<String> itemIdList) {
		this.itemIdList = itemIdList;
	}
}
