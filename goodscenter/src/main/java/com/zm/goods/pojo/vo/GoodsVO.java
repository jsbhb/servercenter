package com.zm.goods.pojo.vo;

import java.util.List;
import java.util.stream.Collectors;

import com.zm.goods.pojo.GoodsFile;

/**
 * @fun 商品的前端展示类
 * @author user
 *
 */
public class GoodsVO {

	private String goodsName;

	private String brand;

	private String goodsId;

	private String description;

	private int type;

	private String origin;

	private String detailPath;

	private double minPrice;

	private double maxPrice;

	private String thirdCategory;

	private String secondCategory;

	private String firstCategory;

	private List<GoodsFile> goodsFileList;

	private List<String> detailList;

	private String accessPath;
	
	private String href;

	private List<GoodsSpecsVO> specsList;

	public void init() {
		minPrice = specsList.stream().map(vo -> vo.getRetailPrice()).collect(Collectors.toList()).stream()
				.min(Double::compareTo).get();
		maxPrice = specsList.stream().map(vo -> vo.getRetailPrice()).collect(Collectors.toList()).stream()
				.max(Double::compareTo).get();
		specsList.stream().forEach(GoodsSpecsVO::init);
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
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

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDetailPath() {
		return detailPath;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
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

	public void setSecondCategory(String sceondCategory) {
		this.secondCategory = sceondCategory;
	}

	public String getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(String firstCategory) {
		this.firstCategory = firstCategory;
	}

	public List<GoodsFile> getGoodsFileList() {
		return goodsFileList;
	}

	public void setGoodsFileList(List<GoodsFile> goodsFileList) {
		this.goodsFileList = goodsFileList;
	}

	public List<String> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<String> detailList) {
		this.detailList = detailList;
	}

	public List<GoodsSpecsVO> getSpecsList() {
		return specsList;
	}

	public void setSpecsList(List<GoodsSpecsVO> specsList) {
		this.specsList = specsList;
	}

}
