/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.GoodsBaseEntity;

/**
 * ClassName: GoodsBaseService <br/>
 * Function: 基础商品服务类. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GoodsBaseService {

	/**
	 * queryByPage:分页查询品牌信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<GoodsBaseEntity> queryByPage(GoodsBaseEntity entity);

	/**
	 * queryById:根据编号查询品牌. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	GoodsBaseEntity queryById(int id);

	/**
	 * saveBrand:保存品牌. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void saveEntity(GoodsBaseEntity entity);

}
