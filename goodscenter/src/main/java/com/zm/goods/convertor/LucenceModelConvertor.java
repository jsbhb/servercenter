package com.zm.goods.convertor;

import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.dto.GoodsSearch;

public class LucenceModelConvertor {
	
	public static void convertToGoodsSearch(GoodsItem item,GoodsSearch searchModel){
		searchModel.setGoodsId(item.getGoodsId());
		searchModel.setBrand(item.getBrand());
		searchModel.setStatus(item.getStatus());
		searchModel.setOrigin(item.getOrigin());
		searchModel.setFirstCategory(item.getFirstCategory());
		searchModel.setThirdCategory(item.getThirdCategory());
		searchModel.setSecondCategory(item.getSecondCategory());
		searchModel.setGoodsName(item.getCustomGoodsName());
		searchModel.setPopular(item.getPopular());
		searchModel.setType(item.getType());
		searchModel.setCreateTime(item.getCreateTime());
		searchModel.setRatio(item.getGoodsTagRatio());
	}

}
