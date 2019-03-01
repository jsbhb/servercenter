/**  
 * Project Name:goodscenter  
 * File Name:BrandMapper.java  
 * Package Name:com.zm.goods.bussiness.dao  
 * Date:Nov 9, 20178:48:41 PM  
 *  
 */
package com.zm.goods.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsExtensionEntity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsPriceRatioEntity;
import com.zm.goods.pojo.GoodsRatioPlatformEntity;
import com.zm.goods.pojo.GoodsShelveRecordEntity;

/**
 * ClassName: BrandMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Nov 9, 2017 8:48:41 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsItemMapper {
	
	/**
	 * @fun 批量更新商品图片
	 * @param initFileList
	 */
	void updateFilesBatch(List<GoodsFile> initFileList);

	/**  
	 * insertFiles:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param files  
	 * @since JDK 1.7  
	 */
	void insertFiles(List<GoodsFile> files);
	/**  
	 * insertFiles:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param files  
	 * @since JDK 1.7  
	 */
	void updateFiles(GoodsFile file);

	/**  
	 * insertStock:插入库存. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void insertStockForBatch(List<String> list);

	void deleteListFiles(List<GoodsFile> file);
	
	GoodsExtensionEntity selectGoodsExtensionInfo(GoodsExtensionEntity entity);
	
	void updateOrInsertGoodsExtension(GoodsExtensionEntity entity);

	Page<GoodsRatioPlatformEntity> selectGoodsRatioPlatformForPage(GoodsRatioPlatformEntity entity);

	GoodsRatioPlatformEntity selectGoodsRatioPlatformForEdit(GoodsRatioPlatformEntity entity);
	
	void insertGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity);
	
	void updateGoodsRatioPlatformInfo(GoodsRatioPlatformEntity entity);
	
	void syncGoodsPriceRatioInfo(List<GoodsPriceRatioEntity> list);

	void updateSpecsTpUpdateTimeBySpecsTpIdList(List<String> list);
	
	void insertGoodsShelveRecord(GoodsShelveRecordEntity entity);
}
