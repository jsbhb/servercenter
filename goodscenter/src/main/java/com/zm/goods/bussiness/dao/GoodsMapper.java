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
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.PopularizeDict;
import com.zm.goods.pojo.PriceContrast;
import com.zm.goods.pojo.PriceModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.pojo.vo.TimeLimitActive;
import com.zm.goods.processWarehouse.model.WarehouseModel;

public interface GoodsMapper {

	List<GoodsItem> listGoods(Map<String,Object> param);
	
	List<GoodsFile> listGoodsFile(Map<String,Object> param);
	
	List<GoodsSpecs> listGoodsSpecs(Map<String,Object> param);
	
	List<PriceContrast> listPriceContrast(Map<String,Object> param);
	
	GoodsSpecs getGoodsSpecs(Map<String,Object> param);
	
	GoodsSpecs getGoodsSpecsForOrder(Map<String,Object> param);
	
	List<GoodsSpecs> listGoodsSpecsByItemId(Map<String,Object> param);
	
	Activity getActivity(Map<String,Object> param);
	
	void createGoods(@Param("centerId")Integer centerId);
	
	void createGoodsFile(@Param("centerId")Integer centerId);
	
	void createFirstCategory(@Param("centerId")Integer centerId);
	
	void createSecondCategory(@Param("centerId")Integer centerId);
	
	void createThirdCategory(@Param("centerId")Integer centerId);
	
	void createGoodsItem(@Param("centerId")Integer centerId);
	
	void createGoodsPrice(@Param("centerId")Integer centerId);
	
	void createLayout(@Param("centerId")Integer centerId);
	
	void createPopularizeDict(@Param("centerId")Integer centerId);
	
	void createPopularizeData(@Param("centerId")Integer centerId);
	
	void createActivity(@Param("centerId")Integer centerId);
	
	void createActivityData(@Param("centerId")Integer centerId);
	
	void createPriceContrast(@Param("centerId")Integer centerId);
	
	List<Layout> listLayout(Map<String,Object> param);
	
	List<Activity> listActivityByLayoutId(Map<String,Object> param);
	
	List<ActivityData> listActiveData(Map<String,Object> param);
	
	PopularizeDict getDictByLayoutId(Map<String,Object> param);
	
	List<DictData> listDictData(Map<String,Object> param);
	
	void updateActivitiesStart(Map<String,Object> param);
	
	void updateActivitiesEnd(Map<String,Object> param);
	
	List<Integer> listEndActiveId(@Param("centerId")String centerId);
	
	List<GoodsItem> listGoodsForLucene(Map<String, Object> param);
	
	List<GoodsSpecs> listSpecsForLucene(Map<String,Object> param);
	
	void updateGoodsUpShelves(Map<String,Object> param);

	List<GoodsItem> queryGoodsItem(Map<String, Object> searchParm);
	
	List<GoodsIndustryModel> queryGoodsCategory(@Param("centerId")String centerId);
	
	Tax getTax(Map<String,Object> param);
	
	List<WarehouseModel> listWarehouse(Map<String,Object> param);
	
	void updateStock(Map<String,Object> param);
	
	void updateStockBack(Map<String,Object> param);
	
	List<TimeLimitActive> listLimitTimeData(@Param("centerId")String centerId);
	
	List<GoodsItem> listSpecialGoods(Map<String,Object> param);

	String getGoodsIdByItemId(String itemId);

	void updateThirdWarehouseStock(List<WarehouseStock> list);

	void saveThirdGoods(List<ThirdWarehouseGoods> list);

	List<PriceModel> getCostPrice(List<OrderBussinessModel> list);

	List<OrderBussinessModel> checkStock();

	void updateGoodsDownShelves(Map<String, Object> param);
	
}
