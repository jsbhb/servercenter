package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

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
import com.zm.goods.pojo.PriceModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.CategoryBO;
import com.zm.goods.pojo.bo.GoodsLifeCycleModel;
import com.zm.goods.pojo.bo.ItemCountBO;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.processWarehouse.model.WarehouseModel;

public interface GoodsMapper {

	List<GoodsItem> listGoods(Map<String,Object> param);
	
	List<GoodsFile> listGoodsFile(Map<String,Object> param);
	
	List<GoodsSpecs> listGoodsSpecs(Map<String,Object> param);
	
	GoodsSpecs getGoodsSpecs(String itemId);
	
	List<GoodsSpecs> getGoodsSpecsForOrder(Map<String,Object> param);
	
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
	
	List<GoodsItem> listGoodsForLuceneUpdateTag(List<String> updateTagList);
	
	List<GoodsSpecs> listSpecsForLucene(Map<String,Object> param);
	
	List<GoodsSpecs> listItemTagForLuceneUpdate(List<String> itemIdList);
	
	void updateGoodsUpShelves(Map<String,Object> param);
	
	void updateGoodsItemUpShelves(List<String> itemIdList);

	List<GoodsItem> queryGoodsItem(Map<String, Object> searchParm);
	
	List<GoodsIndustryModel> queryGoodsCategory();
	
	List<Tax> getTax(List<String> list);
	
	List<WarehouseModel> listWarehouse(Map<String,Object> param);
	
	void updateStock(Map<String,Object> param);
	
	void updateStockBack(Map<String,Object> param);
	
	List<String> getGoodsIdByItemId(List<String> itemIdList);

	void updateThirdWarehouseStock(List<WarehouseStock> list);

	void saveThirdGoods(List<ThirdWarehouseGoods> list);

	List<PriceModel> getCostPrice(List<OrderBussinessModel> list);

	List<OrderBussinessModel> checkStock();

	void updateGoodsDownShelves(List<String> goodsIdList);
	
	void updateGoodsItemDownShelves(List<String> itemIdList);
	
	List<ItemCountBO> countUpShelvesStatus(List<String> goodsIdList);
	
	List<String> listFirstCategory(List<String> goodsIdList);
	
	List<String> listSecondCategory(List<String> goodsIdList);
	
	List<String> listThirdCategory(List<String> goodsIdList);

	void updateGoodsItemUnDistribution(List<String> list);
	
	List<OrderBussinessModel> checkStockByItemIds(List<String> itemIdList);

	List<GoodsConvert> listSkuAndConversionByItemId(List<String> list);

	List<CategoryBO> listCategoryByGoodsIds(List<String> goodsIds);

	void updateFirstCategory(Map<String,Object> param);

	void updateSecondCategory(Map<String,Object> param);

	void updateThirdCategory(Map<String,Object> param);

	void updateFirstCategoryHide(List<String> firstList);

	List<String> listHideFirstCategory(Map<String,Object> param);
	List<String> listHideSecondCategory(Map<String,Object> param);
	List<String> listHideThirdCategory(Map<String,Object> param);

	List<GoodsSpecs> listItemUpshelvTagForLuceneUpdate(Map<String, Object> param);

	int countGoodsBySupplierIdAndItemId(Map<String, Object> param);

	int getOrderGoodsType(Map<String, Object> param);

	void insertGoodsLifeCycleBatch(List<GoodsLifeCycleModel> modelList);

	GoodsItem getGoodsItemByGoodsIdForGoodsBillboard(String goodsId);

}
