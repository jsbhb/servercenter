/**  
 * Project Name:goodscenter  
 * File Name:BrandMapper.java  
 * Package Name:com.zm.goods.bussiness.dao  
 * Date:Nov 9, 20178:48:41 PM  
 *  
 */
package com.zm.goods.bussiness.dao;

import com.github.pagehelper.Page;
import com.zm.goods.pojo.BrandEntity;

/**  
 * ClassName: BrandMapper <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Nov 9, 2017 8:48:41 PM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7  
 */
public interface BrandMapper {

	/**  
	 * selectForPage:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<BrandEntity> selectForPage(BrandEntity entity);

	/**  
	 * selectById:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	BrandEntity selectById(int id);

	/**  
	 * insert:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void insert(BrandEntity entity);

}
