/**  
 * Project Name:cardmanager  
 * File Name:GoodsEntity.java  
 * Package Name:com.card.manager.factory.goods.model  
 * Date:Nov 12, 201710:28:15 PM  
 *  
 */
package com.zm.goods.pojo;

import com.zm.goods.common.Pagination;

/**
 * ClassName: GoodsEntity <br/>
 * Function: 商品实体 <br/>
 * date: Nov 12, 2017 10:28:15 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class GoodsEntity extends Pagination {
	private int id;
	private String goodsId;// 商品ID
	private String supplierId;// 商家ID
	private String supplierName;// 商家名称
	private int baseId;// 商品基本信息ID
	private String goodsName;// 商品名称
	private String description;// 描述
	private String origin;// 原产国
	private String status;// 商品状态0：初始，1：可用；2：可分销
	private int type;// 商品分类0：大贸；1：跨境;2：一般贸易
	private int isPopular;// 是否特推0：否，1是
	private int isNew;// 是否新品0：否，1是
	private int isGoods;// 是否精选0：否，1是
	private int isChoice;// 是否渠道商品0：否，1是
	private int ishot;// 是否..0：否，1是
	private int isFreePostFee;// 是否包邮0：否，1是
	private String detailPath;// 详情地址
	private String indexStatus;// 是否创建lucene0：否，1是
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String opt;// 操作人
	private int templateId;
	private GoodsItemEntity goodsItem;
	private int thirdId;
	private GoodsPrice goodsPrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public int getBaseId() {
		return baseId;
	}

	public void setBaseId(int baseId) {
		this.baseId = baseId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsPopular() {
		return isPopular;
	}

	public void setIsPopular(int isPopular) {
		this.isPopular = isPopular;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public int getIsGoods() {
		return isGoods;
	}

	public void setIsGoods(int isGoods) {
		this.isGoods = isGoods;
	}

	public int getIsChoice() {
		return isChoice;
	}

	public void setIsChoice(int isChoice) {
		this.isChoice = isChoice;
	}

	public int getIshot() {
		return ishot;
	}

	public void setIshot(int ishot) {
		this.ishot = ishot;
	}

	public int getIsFreePostFee() {
		return isFreePostFee;
	}

	public void setIsFreePostFee(int isFreePostFee) {
		this.isFreePostFee = isFreePostFee;
	}

	public String getDetailPath() {
		return detailPath;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
	}

	public String getIndexStatus() {
		return indexStatus;
	}

	public void setIndexStatus(String indexStatus) {
		this.indexStatus = indexStatus;
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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public GoodsItemEntity getGoodsItem() {
		return goodsItem;
	}

	public void setGoodsItem(GoodsItemEntity goodsItem) {
		this.goodsItem = goodsItem;
	}

	public int getThirdId() {
		return thirdId;
	}

	public void setThirdId(int thirdId) {
		this.thirdId = thirdId;
	}

	public GoodsPrice getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(GoodsPrice goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
}
