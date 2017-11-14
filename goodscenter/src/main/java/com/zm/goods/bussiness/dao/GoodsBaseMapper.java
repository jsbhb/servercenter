/**  
 * Project Name:goodscenter  
 * File Name:BrandMapper.java  
 * Package Name:com.zm.goods.bussiness.dao  
 * Date:Nov 9, 20178:48:41 PM  
 *  
 */
package com.zm.goods.bussiness.dao;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsBaseEntity;

/**
 * ClassName: GoodsBaseMapper <br/>
 * Function: 基础商品持久层实体. <br/>
 * date: Nov 9, 2017 8:48:41 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsBaseMapper {

	/**
	 * selectForPage:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsBaseEntity> selectForPage(GoodsBaseEntity entity);

	/**
	 * selectById:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsBaseEntity selectById(int id);

	/**
	 * insert:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insert(GoodsBaseEntity entity);

}
