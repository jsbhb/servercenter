package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.Tax;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.CategoryBO;
import com.zm.goods.pojo.bo.GoodsLifeCycleModel;
import com.zm.goods.pojo.bo.ItemCountBO;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.GoodsItem;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.processWarehouse.model.WarehouseModel;

public interface GoodsMapper {

	/**
	 * @fun 根据goodsId获取Goods对象
	 * @param goodsId
	 * @return
	 */
	GoodsItem getGoodsByGoodsId(String goodsId);
	/**
	 * @fun 根据goodsId 获取GoodsSpecsTradePattern对象
	 * @param goodsId
	 * @return
	 */
	List<GoodsSpecsTradePattern> listGoodsSpecsTpByGoodsId(String goodsId);
	/**
	 * @fun 根据specsId 获取GoodsSpecs对象
	 * @param specsIdList
	 * @return
	 */
	List<GoodsSpecs> listGoodsSpecsBySpecsIds(List<String> specsIdList);
	/**
	 * @fun 获取GoodsFile对象
	 * @param param
	 * @return
	 */
	List<GoodsFile> listGoodsFile(Map<String,Object> param);
	/**
	 * @fun 根据specsId List 搜索GoodsspecsTp
	 * @param list
	 * @return
	 */
	List<GoodsSpecs> listGoodsSpecsTpBySpecsTpIds(List<String> list);
	
	GoodsSpecs getGoodsSpecs(String itemId);
	
	List<GoodsSpecs> getGoodsSpecsForOrder(Map<String,Object> param);
	
	GoodsSpecs getGoodsSpecsForButtJoinOrder(String itemId);
	
	List<GoodsItem> listGoodsForLuceneUpdateTag(List<String> updateTagList);
	
	List<GoodsSpecs> listSpecsForLucene(Map<String,Object> param);
	
	List<GoodsSpecs> listItemTagForLuceneUpdate(List<String> itemIdList);
	
	/**
	 * @fun 前端搜索。根据Lucene搜索到的数据进行数据库查询详细信息
	 * @param searchParm
	 * @return
	 */
	List<GoodsSpecsTradePattern> listGoodsSpecsTpForFrontSearch(Map<String, Object> searchParm);
	/**
	 * @fun 前端搜索。根据goodsId搜索goods
	 * @param goodsIds
	 * @return
	 */
	List<GoodsItem> listGoodsItemByGoodsIds(List<String> goodsIds);
	
	List<GoodsIndustryModel> queryGoodsCategory();
	
	List<Tax> getTax(List<String> list);
	/**
	 * @fun 根据itemids获取库存对象
	 * @param itemIds
	 * @return
	 */
	List<WarehouseModel> listWarehouse(List<String> itemIds);
	/**
	 * @fun 根据specsTpId获取该id下的总库存
	 * @param specsTpIds
	 * @return
	 */
	List<WarehouseModel> listWarehouseBySpecsTpIds(List<String> specsTpIds);
	
	void updateStock(Map<String,Object> param);
	
	void updateStockBack(Map<String,Object> param);
	
	String getGoodsIdBySpecsTpId(String specsTpId);

	void updateThirdWarehouseStock(List<WarehouseStock> list);

	void saveThirdGoods(List<ThirdWarehouseGoods> list);

	List<OrderBussinessModel> checkStock();

	void updateGoodsDownShelves(List<String> goodsIdList);
	
	void updateGoodsItemDownShelves(List<String> itemIdList);
	
	List<ItemCountBO> countUpShelvesStatus(List<String> goodsIdList);
	
	List<String> listFirstCategory(List<String> goodsIdList);
	
	List<String> listSecondCategory(List<String> goodsIdList);
	
	List<String> listThirdCategory(List<String> goodsIdList);

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

	List<GoodsItem> listGoodsByGoodsIds(List<String> goodsIdList);

	List<GoodsSearch> listSpecsNeedToCreateIndex(List<String> specsTpIdList);

	/**
	 * @fun 更新一般贸易商品为上架状态
	 * @param specsTpIdList
	 */
	void updateTradeSpecsUpshelf(@Param("list")List<String> specsTpIdList, @Param("display")int display);

}
