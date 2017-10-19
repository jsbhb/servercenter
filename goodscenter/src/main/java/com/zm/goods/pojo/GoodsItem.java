package com.zm.goods.pojo;

import java.util.List;
import java.util.Set;

public class GoodsItem extends GoodsBase {
	
	private Integer id;

	private String goodsId;

	private Integer supplierId;

	private String customGoodsName;
	
	private Integer weight;

	private Integer status;

	private Integer type;

	private Integer popular;

	private Integer hot;

	private Integer good;

	private Integer choice;

	private Integer indexStatus;

	private String detailPath;

	private String createTime;

	private String updateTime;
	
	private Set<String> specsInfo;

	private String opt;
	
	private Double price;
	
	private Double realPrice;
	
	private List<GoodsFile> goodsFileList;

	private List<GoodsSpecs> goodsSpecsList;

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<String> getSpecsInfo() {
		return specsInfo;
	}

	public void setSpecsInfo(Set<String> specsInfo) {
		this.specsInfo = specsInfo;
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

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getCustomGoodsName() {
		return customGoodsName;
	}

	public void setCustomGoodsName(String customGoodsName) {
		this.customGoodsName = customGoodsName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDetailPath() {
		return detailPath;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
	}

	public Integer getPopular() {
		return popular;
	}

	public void setPopular(Integer popular) {
		this.popular = popular;
	}

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public Integer getGood() {
		return good;
	}

	public void setGood(Integer good) {
		this.good = good;
	}

	public Integer getChoice() {
		return choice;
	}

	public void setChoice(Integer choice) {
		this.choice = choice;
	}

	public Integer getIndexStatus() {
		return indexStatus;
	}

	public void setIndexStatus(Integer indexStatus) {
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

	@Override
	public String toString() {
		return "GoodsItem [goodsId=" + goodsId + ", supplierId=" + supplierId + ", customGoodsName=" + customGoodsName
				+ ", status=" + status + ", type=" + type + ", popular=" + popular + ", hot=" + hot + ", good=" + good
				+ ", choice=" + choice + ", indexStatus=" + indexStatus + ", detailPath=" + detailPath + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", specsInfo=" + specsInfo + ", opt=" + opt
				+ ", goodsFileList=" + goodsFileList + ", goodsSpecsList=" + goodsSpecsList + "]";
	}

}
