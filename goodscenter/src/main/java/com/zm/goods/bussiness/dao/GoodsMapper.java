package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.goods.pojo.GoodsConvert;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.OrderBussinessModel;
import com.zm.goods.pojo.ThirdWarehouseGoods;
import com.zm.goods.pojo.WarehouseStock;
import com.zm.goods.pojo.bo.AutoSelectionBO;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsPricePO;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.pojo.vo.GoodsIndustryModel;
import com.zm.goods.processWarehouse.model.WarehouseModel;

public interface GoodsMapper {

	/**
	 * @fun 根据goodsId获取Goods对象
	 * @param goodsId
	 * @return
	 */
	Goods getGoodsByGoodsId(String goodsId);
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
	List<GoodsSpecsTradePattern> listGoodsSpecsTpBySpecsTpIds(List<String> list);
	/**
	 * @fun 根据goodsId 获取商品是跨境还是一般贸易
	 * @param goodsId
	 * @return
	 */
	int getGoodsTypeByGoodsId(String goodsId);
	/**
	 * @fun 根据itemIds获取kj_goods_item表信息
	 * @param itemIds
	 * @return
	 */
	List<Items> listItemsByItemIds(List<String> itemIds);
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
	List<Goods> listGoodsItemByGoodsIds(List<String> goodsIds);
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
	/**
	 * @fun 根据specsTpIds 查询 Items
	 * @param specsTpIds
	 * @return
	 */
	List<Items> listItemsBySpecsTpIds(List<String> specsTpIds);
	/**
	 * @fun 更新一般贸易商品为上架状态
	 * @param specsTpIdList
	 */
	void updateTradeSpecsUpshelf(@Param("list")List<String> specsTpIdList, @Param("display")int display);
	/**
	 * @fun 单个跨境商品更新上架状态
	 * @param bo
	 * @param display
	 */
	void updateSignalKjGoodsUpShelves(@Param("bo")AutoSelectionBO bo, @Param("display")int display);
	/**
	 * @fun 批量更新跨境商品上架状态
	 * @param boList
	 * @param display
	 */
	void updateBatchKjGoodsUpShelves(@Param("list")List<AutoSelectionBO> boList, @Param("display")int display);
	/**
	 * @fun 更新specsTp下架
	 * @param specsTpIdList
	 */
	void updateSpecsTpDownShelves(List<String> specsTpIdList);
	/**
	 * @fun 根据ItemIds和是否分销查询specsTP
	 * @param param
	 * @return
	 */
	List<GoodsSpecsTradePattern> getGoodsSpecsTpByParam(Map<String, Object> param);
	/**
	 * @fun 根据Itemids获取价格信息
	 * @param itemIds
	 * @return
	 */
	List<GoodsPricePO> listGoodsPriceByItemIds(List<String> itemIds);
	/**
	 * @fun 根据SpecsTpIds获取价格信息
	 * @param specsTpIds
	 * @return
	 */
	List<GoodsPricePO> listGoodsPriceBySpecsTpIds(List<String> specsTpIds);
	/**
	 * @fun 获取三级分类
	 * @return
	 */
	List<GoodsIndustryModel> queryGoodsCategory();
	/**
	 * @fun 更新库存
	 * @param param
	 */
	void updateStock(Map<String,Object> param);
	/**
	 * @fun 库存回滚
	 * @param param
	 */
	void updateStockBack(Map<String,Object> param);
	/**
	 * @fun 根据specsTpId获取goodsId
	 * @param specsTpId
	 * @return
	 */
	String getGoodsIdBySpecsTpId(String specsTpId);
	/**
	 * @fun 根据同步到的第三方库存更新本地库存
	 * @param list
	 */
	void updateThirdWarehouseStock(List<WarehouseStock> list);
	/**
	 * @fun 保存第三方商品
	 * @param list
	 */
	void saveThirdGoods(List<ThirdWarehouseGoods> list);
	/**
	 * @fun 定时同步库存时使用
	 * @return
	 */
	List<OrderBussinessModel> checkStock();
	/**
	 * @fun 同步库存
	 * @param itemIdList
	 * @return
	 */
	List<OrderBussinessModel> checkStockByItemIds(List<String> itemIdList);
	/**
	 * @fun 获取sku和换算比例
	 * @param list
	 * @return
	 */
	List<GoodsConvert> listSkuAndConversionByItemId(List<String> list);
	/**
	 * @fun 判断订单商品是否属于同一仓库
	 * @param param
	 * @return
	 */
	int countGoodsBySupplierIdAndItemId(Map<String, Object> param);
	/**
	 * @fun 获取商品贸易类型
	 * @param param
	 * @return
	 */
	List<Integer> getOrderGoodsType(Map<String, Object> param);
	/**
	 * @fun 根据goodsId获取商品基本信息
	 * @param goodsIdList
	 * @return
	 */
	List<Goods> listGoodsByGoodsIds(List<String> goodsIdList);
	/**
	 * @fun 根据specsTpIdList 获取需要建lucene索引的数据
	 * @param specsTpIdList
	 * @return
	 */
	List<GoodsSearch> listSpecsNeedToCreateIndex(List<String> specsTpIdList);
	
}
