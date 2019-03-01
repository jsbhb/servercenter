/**  
 * Project Name:goodscenter  
 * File Name:BrandMapper.java  
 * Package Name:com.zm.goods.bussiness.dao  
 * Date:Nov 9, 20178:48:41 PM  
 *  
 */
package com.zm.goods.bussiness.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsInfoListForDownload;
import com.zm.goods.pojo.GoodsListDownloadParam;
import com.zm.goods.pojo.GoodsRebateEntity;
import com.zm.goods.pojo.GoodsTagEntity;
import com.zm.goods.pojo.TagFuncEntity;
import com.zm.goods.pojo.po.Goods;
import com.zm.goods.pojo.po.GoodsPricePO;
import com.zm.goods.pojo.po.GoodsSpecs;
import com.zm.goods.pojo.po.GoodsSpecsTradePattern;
import com.zm.goods.pojo.po.Items;
import com.zm.goods.pojo.vo.BackGoodsItemVO;
import com.zm.goods.pojo.vo.BackGoodsVO;
import com.zm.goods.pojo.vo.BackItemsVO;
import com.zm.goods.pojo.vo.GoodsSpecsVO;
import com.zm.goods.processWarehouse.model.WarehouseModel;

/**
 * ClassName: BrandMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Nov 9, 2017 8:48:41 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsBackMapper {

	/**
	 * @fun 插入goodsSpecsTp
	 * @param goodsSpecsTradePattern
	 */
	void insertSpecsTpBatch(List<GoodsSpecsTradePattern> list);

	/**
	 * insert:插入商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insertGoods(Goods goods);
	/**
	 * @fun 批量插入供应商商品
	 * @param items
	 */
	void insertItemBatch(List<Items> list);
	/**
	 * @fun 插入规格
	 * @param specs
	 */
	void insertSpecsBatch(List<GoodsSpecs> specsList);
	/**
	 * @fun 批量插入价格
	 * @param priceList
	 */
	void insertItemPriceBatch(List<GoodsPricePO> priceList);
	/**
	 * @fun 批量插入库存
	 * @param stockList
	 */
	void insertStockBatch(List<WarehouseModel> stockList);
	/**
	 * @fun 更新商品Goods
	 * @param goods
	 */
	void updateGoods(Goods goods);
	/**
	 * @fun 更新goodsSpecsTp
	 * @param goodsSpecsTradePattern
	 */
	void updateSpecsTp(GoodsSpecsTradePattern goodsSpecsTradePattern);
	/**
	 * @fun 批量更新供应商商品
	 * @param itemsList
	 */
	void updateItemBatch(List<Items> itemsList);
	/**
	 * @fun 更新商品规格
	 * @param specs
	 */
	void updateSpecs(GoodsSpecs specs);
	/**
	 * @fun 批量更新价格
	 * @param priceList
	 */
	void updateItemPriceBatch(List<GoodsPricePO> priceList);
	/**
	 * @fun 批量更新库存
	 * @param stockList
	 */
	void updateStockBatch(List<WarehouseModel> stockList);
	/**
	 * @fun 根据specsTpId查询返佣
	 * @param specsTpId
	 * @return
	 */
	List<GoodsRebateEntity> selectGoodsRebateBySpecsTpId(String specsTpId);
	/**
	 * @fun 插入返佣
	 * @param list
	 */
	void insertGoodsRebate(List<GoodsRebateEntity> list);
	/**
	 * selectForPage:分页查询商品标签. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsTagEntity> selectTagForPage(GoodsTagEntity entity);
	/**
	 * @fun 新增标签
	 * @param entity
	 */
	void insertGoodsTag(GoodsTagEntity entity);
	/**
	 * @fun 更新标签
	 * @param entity
	 */
	void updateGoodsTag(GoodsTagEntity entity);
	/**
	 * @fun 删除标签
	 * @param entity
	 */
	void deleteGoodsTag(GoodsTagEntity entity);
	/**
	 * @fun 判断该标签ID有没有绑定商品
	 * @param id
	 * @return
	 */
	int checkGoodsTagUsing(Integer id);
	/**
	 * @fun 查询所有标签
	 * @return
	 */
	List<GoodsTagEntity> selectTagListInfo();
	/**
	 * @fun 更新 权重
	 * @param list
	 */
	void updateSpecsTpTagRatioByList(List<GoodsSpecsTradePattern> list);

	/**  
	 * updateDetailPath:(更新商品详情). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateDetailPath(Goods entity);
	/**
	 * @fun 查询标签功能
	 * @return
	 */
	List<TagFuncEntity> selectTagFuncListInfo();

	List<GoodsInfoListForDownload> selectGoodsListForDownload(GoodsListDownloadParam param);

	List<GoodsFile> selectGoodsFileByParam(Map<String,Object> param);

	List<GoodsInfoListForDownload> selectGoodsListForDownloadPartOne(GoodsListDownloadParam param);

	List<GoodsInfoListForDownload> selectGoodsListForDownloadPartTwo(GoodsListDownloadParam param);

	List<GoodsInfoListForDownload> selectGoodsListForDownloadPartThree(GoodsListDownloadParam param);

	List<String> getGoodsPicPath(String goodsId);

	/**
	 * @fun 更新零售价
	 * @param param
	 */
	void updateRetailPrice(@Param("param")Map<String, Object> param);
	/**
	 * @fun 根据条码查询商品规格
	 * @param list
	 * @return
	 */
	List<GoodsSpecs> listGoodsSpecsByEnCodeList(List<String> list);
	/**
	 * @fun 根据规格ID 和 贸易类型（一般贸易、跨境）来查询specsTp
	 * @param param
	 * @return
	 */
	List<GoodsSpecsTradePattern> listGoodsSpecsTradPatternByParam(Map<String, Object> param);
	/**
	 * @fun 根据三级分类和品牌查询goods
	 * @param param
	 * @return
	 */
	List<Goods> listGoodsByCategoryAndBrand(Map<String, String> param);
	/**
	 * @fun 更新供应商商品通过审核
	 * @param item
	 */
	void updateItemStatusPass(Items item);
	/**
	 * @fun 更新specsTp为下架状态（如果是初始化状态的话）
	 * @param specsTpId
	 */
	void updateSpecsTpCanBeUpShelf(String specsTpId);
	/**
	 * @fun 更新供应商商品未通过审核
	 * @param item
	 */
	void updateItemStatusUnPass(Items item);
	/**
	 * @fun 更新specsTp为初始状态
	 * @param specsTpId
	 */
	void updateSpecsTpInit(String specsTpId);
	/**
	 * @fun 更新福利商城是否显示
	 * @param param
	 */
	void updateWelfareDisplay(Map<String,Object> param);
	/**
	 * @fun 获取供应商商品
	 * @param item
	 * @return
	 */
	Page<BackGoodsItemVO> listItems4Page(BackGoodsItemVO item);
	/**
	 * @fun 获取商品列表
	 * @param item
	 * @return
	 */
	Page<BackGoodsVO> listGoods4Page(Map<String,Object> param);
	/**
	 * @fun 根据goodsId 和状态获取goods对应下的规格
	 * @param param
	 * @return
	 */
	List<GoodsSpecsVO> listGoodsSpecsTpByGoodsIdAndStatus(Map<String, Object> param);
	/**
	 * @fun 根据specsTpId 获取items
	 * @param specsTpId
	 * @return
	 */
	List<BackItemsVO> listItemBySpecsTpId(String specsTpId);
}
