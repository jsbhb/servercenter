package com.zm.goods.pojo.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zm.goods.annotation.SearchCondition;

/**
 * @fun 搜索条件实体类
 * @author user
 *
 */
public class GoodsSearch {

	private String specsTpId;

	private Integer centerId;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String goodsName;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String upShelves;

	private Double price;

	private String upshelfTime;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String thirdCategory;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String secondCategory;

	@SearchCondition(value = SearchCondition.SEARCH)
	private String firstCategory;

	@SearchCondition(value = SearchCondition.FILTER)
	private String priceMin;

	@SearchCondition(value = SearchCondition.FILTER)
	private String priceMax;

	private Integer ratio;

	@SearchCondition(value = SearchCondition.FILTER)
	private Integer welfare;

	private Integer saleNum;

	@SearchCondition(value = SearchCondition.FILTER)
	private List<GuideProperty> guideList;

	public Map<String, String> initGuideProperty() {
		if (guideList != null) {
			Map<String, String> tmp = new HashMap<>();
			for (GuideProperty pr : guideList) {
				if (tmp.get(pr.getName()) == null) {
					tmp.put(pr.getName(), pr.getValue());
				} else {
					String str = tmp.get(pr.getName());
					str = str + "," + pr.getValue();
					tmp.put(pr.getName(), str);
				}
			}
			return tmp;
		}
		return null;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public List<GuideProperty> getGuideList() {
		return guideList;
	}

	public void setGuideList(List<GuideProperty> guideList) {
		this.guideList = guideList;
	}

	public Integer getRatio() {
		return ratio;
	}

	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}

	public String getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(String priceMin) {
		this.priceMin = priceMin;
	}

	public String getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}

	public String getUpShelves() {
		return upShelves;
	}

	public void setUpShelves(String upShelves) {
		this.upShelves = upShelves;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecsTpId() {
		return specsTpId;
	}

	public void setSpecsTpId(String specsTpId) {
		this.specsTpId = specsTpId;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUpshelfTime() {
		return upshelfTime;
	}

	public void setUpshelfTime(String upshelfTime) {
		this.upshelfTime = upshelfTime;
	}

	public Integer getWelfare() {
		return welfare;
	}

	public void setWelfare(Integer welfare) {
		this.welfare = welfare;
	}
}
