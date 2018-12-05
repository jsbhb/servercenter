package com.zm.goods.activity.backmanger.model;


public class BargainActivityGoodsModel extends BaseActivityGoodsModel {

	private Integer firstMinRatio;
	
	private Integer firstMaxRatio;
	
	private Integer minRatio;
	
	private Integer maxRatio;
	
	private Double lessMinPrice;

	public Integer getFirstMinRatio() {
		return firstMinRatio;
	}

	public void setFirstMinRatio(Integer firstMinRatio) {
		this.firstMinRatio = firstMinRatio;
	}

	public Integer getFirstMaxRatio() {
		return firstMaxRatio;
	}

	public void setFirstMaxRatio(Integer firstMaxRatio) {
		this.firstMaxRatio = firstMaxRatio;
	}

	public Integer getMinRatio() {
		return minRatio;
	}

	public void setMinRatio(Integer minRatio) {
		this.minRatio = minRatio;
	}

	public Integer getMaxRatio() {
		return maxRatio;
	}

	public void setMaxRatio(Integer maxRatio) {
		this.maxRatio = maxRatio;
	}

	public Double getLessMinPrice() {
		return lessMinPrice;
	}

	public void setLessMinPrice(Double lessMinPrice) {
		this.lessMinPrice = lessMinPrice;
	}
}
