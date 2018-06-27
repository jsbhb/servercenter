package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zm.goods.pojo.Activity;
import com.zm.goods.pojo.ActivityData;
import com.zm.goods.pojo.DictData;
import com.zm.goods.pojo.GoodsConvert;
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
import com.zm.goods.pojo.bo.CategoryBO;
import com.zm.goods.pojo.bo.ItemCountBO;
import com.zm.goods.pojo.bo.ItemStockBO;
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
	
	GoodsSpecs getGoodsSpecsForButtJoinOrder(String itemId);
	
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
	
	void updateGoodsItemUpShelves(Map<String,Object> param);

	List<GoodsItem> queryGoodsItem(Map<String, Object> searchParm);
	
	List<GoodsIndustryModel> queryGoodsCategory(@Param("centerId")String centerId);
	
	Tax getTax(Map<String,Object> param);
	
	List<WarehouseModel> listWarehouse(Map<String,Object> param);
	
	void updateStock(Map<String,Object> param);
	
	void updateStockBack(Map<String,Object> param);
	
	List<TimeLimitActive> listLimitTimeData(@Param("centerId")String centerId);
	
	List<GoodsItem> listSpecialGoods(Map<String,Object> param);

	List<String> getGoodsIdByItemId(Map<String,Object> param);

	void updateThirdWarehouseStock(List<WarehouseStock> list);

	void saveThirdGoods(List<ThirdWarehouseGoods> list);

	List<PriceModel> getCostPrice(List<OrderBussinessModel> list);

	List<OrderBussinessModel> checkStock();

	void updateGoodsDownShelves(Map<String, Object> param);
	
	void updateGoodsItemDownShelves(Map<String, Object> param);
	
	List<ItemCountBO> countUpShelvesStatus(Map<String, Object> param);
	
	void insertCenterGoods(Map<String, Object> param);
	
	void insertCenterGoodsFile(Map<String, Object> param);
	
	void insertCenterFirstCategory(Map<String, Object> param);
	
	void insertCenterSecondCategory(Map<String, Object> param);
	
	void insertCenterThirdCategory(Map<String, Object> param);
	
	void insertCenterGoodsItem(Map<String, Object> param);
	
	void insertCenterGoodsPrice(Map<String, Object> param);
	
	void insert2BGoodsPrice(Map<String, Object> param);
	
	List<String> listFirstCategory(List<String> goodsIdList);
	
	List<String> listSecondCategory(List<String> goodsIdList);
	
	List<String> listThirdCategory(List<String> goodsIdList);

	List<ItemCountBO> countItem(Map<String, Object> param);

	void updateGoodsItemUnDistribution(List<String> list);
	
	List<OrderBussinessModel> checkStockByItemIds(List<String> itemIdList);

	List<GoodsConvert> listSkuAndConversionByItemId(List<String> list);

	List<String> listItemIdsByGoodsId(String goodsId);

	List<ItemStockBO> listStockByItemIds(List<String> itemIds);
	
	List<CategoryBO> listCategoryByGoodsIds(List<String> goodsIds);

	void updateFirstCategory(Map<String,Object> param);

	void updateSecondCategory(Map<String,Object> param);

	void updateThirdCategory(Map<String,Object> param);

	void updateFirstCategoryHide(List<String> firstList);

	List<String> listHideFirstCategory(Map<String,Object> param);
	List<String> listHideSecondCategory(Map<String,Object> param);
	List<String> listHideThirdCategory(Map<String,Object> param);

}
