/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.BrandEntity;

/**
 * ClassName: BrandService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface BrandService {

	/**
	 * queryByPage:分页查询品牌信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<BrandEntity> queryByPage(BrandEntity entity);

	/**
	 * queryById:根据编号查询品牌. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	BrandEntity queryById(int id);

	/**
	 * saveBrand:保存品牌. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void saveBrand(BrandEntity entity);

}
