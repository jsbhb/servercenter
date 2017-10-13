package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.ActivityData;
import com.zm.goods.pojo.DictData;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItem;
import com.zm.goods.pojo.GoodsSpecs;
import com.zm.goods.pojo.Layout;
import com.zm.goods.pojo.PopularizeDict;
import com.zm.goods.pojo.PriceContrast;

public interface GoodsMapper {

	List<GoodsItem> listGoods(Map<String,Object> param);
	
	List<GoodsFile> listGoodsFile(Map<String,Object> param);
	
	List<GoodsSpecs> listGoodsSpecs(Map<String,Object> param);
	
	List<PriceContrast> listPriceContrast(Map<String,Object> param);
	
	GoodsSpecs getGoodsSpecs(Map<String,Object> param);
	
	List<GoodsSpecs> listGoodsSpecsByItemId(Map<String,Object> param);
	
	Activity getActivity(Map<String,Object> param);
	
	void createGoodsItem(@Param("centerId")Integer centerId);
	
	void createGoodsFile(@Param("centerId")Integer centerId);
	
	void createFirstCategory(@Param("centerId")Integer centerId);
	
	void createSecondCategory(@Param("centerId")Integer centerId);
	
	void createThirdCategory(@Param("centerId")Integer centerId);
	
	void createBrand(@Param("centerId")Integer centerId);
	
	void createGoodsSpecs(@Param("centerId")Integer centerId);
	
	void createGoodsPrice(@Param("centerId")Integer centerId);
	
	void createGoodsStock(@Param("centerId")Integer centerId);
	
	void createLayout(@Param("centerId")Integer centerId);
	
	void createPopularizeDict(@Param("centerId")Integer centerId);
	
	void createPopularizeData(@Param("centerId")Integer centerId);
	
	void createActivity(@Param("centerId")Integer centerId);
	
	void createActivityData(@Param("centerId")Integer centerId);
	
	void createPriceContrast(@Param("centerId")Integer centerId);
	
	List<Layout> listLayout(Map<String,Object> param);
	
	Activity getActivityByLayoutId(Map<String,Object> param);
	
	List<ActivityData> listActiveData(Map<String,Object> param);
	
	PopularizeDict getDictByLayoutId(Map<String,Object> param);
	
	List<DictData> listDictData(Map<String,Object> param);
	
	void updateActivitiesStart(Map<String,Object> param);
	
	void updateActivitiesEnd(Map<String,Object> param);
	
}
