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
import com.zm.goods.pojo.GoodsExtensionEntity;
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

	GoodsExtensionEntity queryGoodsExtensionInfo(GoodsExtensionEntity entity);

	void updateGoodsExtension(GoodsExtensionEntity entity);
	
	Page<GoodsRatioPlatformEntity> queryGoodsRatioPlanformPage(GoodsRatioPlatformEntity entity);

	GoodsRatioPlatformEntity queryGoodsRatioPlanformInfo(GoodsRatioPlatformEntity entity);

	void createGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity);

	void updateGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity);

	void syncGoodsPriceRatioInfo(List<GoodsPriceRatioEntity> list);

	ResultModel syncStockQtyNotEnoughItemList();

	ResultModel syncStockQtyEnoughItemList();

}
