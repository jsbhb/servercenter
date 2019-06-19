package com.zm.goods.pojo.vo;

import java.util.List;

import com.zm.goods.pojo.GoodsTagEntity;
/**
 * @fun 微店筛选条件
 * @author user
 *
 */
public class ShopManagerSearchConditions {

	private List<GoodsIndustryModel> firstList;
	private List<GoodsTagEntity> tagList;
	public List<GoodsIndustryModel> getFirstList() {
		return firstList;
	}
	public void setFirstList(List<GoodsIndustryModel> firstList) {
		this.firstList = firstList;
	}
	public List<GoodsTagEntity> getTagList() {
		return tagList;
	}
	public void setTagList(List<GoodsTagEntity> tagList) {
		this.tagList = tagList;
	}
	
}
