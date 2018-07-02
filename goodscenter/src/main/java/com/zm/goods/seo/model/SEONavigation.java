package com.zm.goods.seo.model;

import java.util.List;

import com.zm.goods.enummodel.SystemEnum;
import com.zm.goods.pojo.vo.GoodsIndustryModel;

public class SEONavigation extends SEOBase{

	private String code;
	private List<GoodsIndustryModel> data;
	public SEONavigation(String code, SystemEnum system, List<GoodsIndustryModel> data){
		super(system);
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<GoodsIndustryModel> getData() {
		return data;
	}
	public void setData(List<GoodsIndustryModel> data) {
		this.data = data;
	}
	
}
