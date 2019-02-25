package com.zm.goods.pojo.po;

import java.util.List;

import com.zm.goods.pojo.GoodsFile;

public class Goods {

	private Integer id;

	private String goodsId;

	private String goodsName;
	
	private int type;//0:跨境,2:一般贸易
	
	private String brandId;
	
	private String brandName;
	
	private String origin;
	
	private String subtitle;

	private String description;

	private String detailPath;

	private String createTime;

	private String updateTime;

	private String thirdCategory;

	private String secondCategory;

	private String firstCategory;

	private List<GoodsFile> goodsFileList;

	private List<GoodsSpecs> goodsSpecsList;

	private String accessPath;
	
	private List<String> detailList;
	
	private String opt;
	
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<String> detailList) {
		this.detailList = detailList;
	}

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<GoodsFile> getGoodsFileList() {
		return goodsFileList;
	}

	public void setGoodsFileList(List<GoodsFile> goodsFileList) {
		this.goodsFileList = goodsFileList;
	}

	public List<GoodsSpecs> getGoodsSpecsList() {
		return goodsSpecsList;
	}

	public void setGoodsSpecsList(List<GoodsSpecs> goodsSpecsList) {
		this.goodsSpecsList = goodsSpecsList;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getDetailPath() {
		return detailPath;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

}
