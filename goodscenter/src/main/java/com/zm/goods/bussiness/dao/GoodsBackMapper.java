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
import com.zm.goods.pojo.GoodsEntity;
import com.zm.goods.pojo.ThirdWarehouseGoods;

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
	 * selectForPage:分页查询商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsEntity> selectForPage(GoodsEntity entity);
	
	/**
	 * selectThirdForPage:分页查询同步商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<ThirdWarehouseGoods> selectThirdForPage(ThirdWarehouseGoods entity);

	/**
	 * selectById:根据商品编号检索商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsEntity selectById(int id);

	/**
	 * insert:插入商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insert(GoodsEntity entity);

	/**
	 * selectAll:检索所有商品. <br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	List<GoodsEntity> selectAll();

	/**  
	 * selectThird:检索供应商商品. <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	ThirdWarehouseGoods selectThird(ThirdWarehouseGoods entity);

	/**  
	 * updateThirdStatus:更新三方商品状态. <br/>  
	 *  
	 * @author hebin  
	 * @param thirdId  
	 * @since JDK 1.7  
	 */
	void updateThirdStatus(int thirdId);

	/**
	 * insert:插入商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void update(GoodsEntity entity);

	/**
	 * insert:插入商品. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void delete(GoodsEntity entity);

	/**
	 * selectById:根据商品编号检索商品. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsEntity selectRecordForDel(GoodsEntity entity);

	/**  
	 * updateDetailPath:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateDetailPath(GoodsEntity entity);

}
