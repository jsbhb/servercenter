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

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.GoodsFile;
import com.zm.goods.pojo.GoodsItemEntity;
import com.zm.goods.pojo.GoodsPrice;

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
	 * selectForPage:分页查询商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsItemEntity> selectForPage(GoodsItemEntity entity);

	/**
	 * selectById:根据商品编号检索商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsItemEntity selectById(int id);

	/**
	 * insert:插入商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insert(GoodsItemEntity entity);

	/**
	 * selectAll:检索所有商品. <br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	List<GoodsItemEntity> selectAll();

	/**  
	 * insertPrice:新增价格. <br/>  
	 *  
	 * @author hebin  
	 * @param goodsPrice  
	 * @since JDK 1.7  
	 */
	void insertPrice(GoodsPrice goodsPrice);

	/**  
	 * updateStatus:更新状态. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateStatus(GoodsItemEntity entity);
	
	/**
	 * 
	 * updatePrice:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7
	 */
	void updatePrice(GoodsPrice entity);

	/**  
	 * insertStock:插入库存. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void insertStock(GoodsItemEntity entity);

	/**  
	 * queryCenterByPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @param centerId
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<GoodsItemEntity> selectCenterForPage(Map<String,Object> params);

	/**  
	 * insertFiles:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param files  
	 * @since JDK 1.7  
	 */
	void insertFiles(List<GoodsFile> files);
	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void update(GoodsItemEntity entity);
	/**
	 * insert:插入商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void delete(GoodsEntity entity);

	/**  
	 * insertFiles:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param files  
	 * @since JDK 1.7  
	 */
	void updateFiles(GoodsFile file);

}
