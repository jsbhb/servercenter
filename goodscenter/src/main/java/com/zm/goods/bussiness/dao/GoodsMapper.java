package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.PriceContrast;

public interface GoodsMapper {

	List<GoodsItem> listBigTradeGoods(Map<String,Object> param);
	
	List<GoodsFile> listGoodsFile(Map<String,Object> param);
	
	List<GoodsSpecs> listGoodsSpecs(List<Integer> list);
	
	List<PriceContrast> listPriceContrast(Map<String,Object> param);
	
	GoodsSpecs getGoodsSpecs(String itemId);
}
