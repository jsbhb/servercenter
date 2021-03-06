/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsExtensionEntity;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;
import com.zm.goods.pojo.GoodsPriceRatioEntity;
import com.zm.goods.pojo.GoodsRatioPlatformEntity;
import com.zm.goods.pojo.ResultModel;

/**
 * ClassName: GoodsItemService <br/>
 * Function: 商品明细服务类. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsItemService {

	/**
	 * queryByPage:分页查询商品信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsItemEntity> queryByPage(GoodsItemEntity entity);

	/**
	 * queryById:根据编号查询商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsItemEntity queryById(int id);

	/**
	 * saveBrand:保存商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void save(GoodsItemEntity entity);

	/**  
	 * notBeFx:设置商品明细不可分销. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void notBeFx(GoodsItemEntity entity);

	/**  
	 * beFx:设置商品明细可以分销. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void beFx(GoodsItemEntity entity);

	/**  
	 * queryCenterByPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin
	 * @param entity
	 * @param parseInt
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<GoodsItemEntity> queryCenterByPage(GoodsItemEntity entity, int centerId);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void update(GoodsItemEntity entity);

	/**  
	 * queryCenterByPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin
	 * @param entity
	 * @param parseInt
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<GoodsItemEntity> queryPurchaseCenterItem(GoodsItemEntity entity);

	/**  
	 * queryCenterByPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin
	 * @param entity
	 * @param parseInt
	 * @return  
	 * @since JDK 1.7  
	 */
	GoodsPrice queryPurchaseCenterItemForEdit(GoodsItemEntity entity);

	/**  
	 * queryCenterByPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin
	 * @param entity
	 * @param parseInt
	 * @return  
	 * @since JDK 1.7  
	 */
	GoodsPrice queryItemPrice(String itemId);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateItemPrice(GoodsPrice entity);

	/**
	 * queryByPage:分页查询商品信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsEntity> queryByPageDownload(GoodsItemEntity entity);

	/**  
	 * queryCenterByPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin
	 * @param entity
	 * @param parseInt
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<GoodsEntity> queryCenterByPageDownload(GoodsItemEntity entity, int centerId);

	/**  
	 * beUse:设置商品明细不可用. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void batchBeUse(GoodsItemEntity entity);

	/**  
	 * beUse:设置商品明细不可用. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void batchBeFx(GoodsItemEntity entity);

	void batchNotBeFx(GoodsItemEntity entity);

	Page<GoodsExtensionEntity> queryGoodsExtensionByPageDownload(GoodsItemEntity entity);

	Page<GoodsExtensionEntity> queryGoodsExtensionCenterByPageDownload(GoodsItemEntity entity, int centerId);

	GoodsExtensionEntity queryGoodsExtensionInfo(GoodsExtensionEntity entity);

	void updateGoodsExtension(GoodsExtensionEntity entity);
	
	List<GoodsPriceRatioEntity> queryGoodsPriceRatioListInfo(GoodsItemEntity entity);

	Page<GoodsRatioPlatformEntity> queryGoodsRatioPlanformPage(GoodsRatioPlatformEntity entity);

	GoodsRatioPlatformEntity queryGoodsRatioPlanformInfo(GoodsRatioPlatformEntity entity);

	void createGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity);

	void updateGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity);

	void syncGoodsPriceRatioInfo(List<GoodsPriceRatioEntity> list);

	ResultModel syncStockQtyNotEnoughItemList();

	ResultModel syncStockQtyEnoughItemList();

}
