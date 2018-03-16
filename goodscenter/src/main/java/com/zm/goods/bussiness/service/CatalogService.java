/**  
 * Project Name:goodscenter  
 * File Name:BrandService.java  
 * Package Name:com.zm.goods.bussiness.service  
 * Date:Nov 9, 20178:37:03 PM  
 *  
 */
package com.zm.goods.bussiness.service;

import java.util.List;

import com.zm.goods.pojo.FirstCatalogEntity;
import com.zm.goods.pojo.SecondCatalogEntity;
import com.zm.goods.pojo.ThirdCatalogEntity;

/**
 * ClassName: CatalogService <br/>
 * Function: 分类服务. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface CatalogService {

	/**
	 * queryAll:根据编号查询分类. <br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	List<FirstCatalogEntity> queryAll();

	/**
	 * saveSecondCatalog:保存一级分类. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void saveSecondCatalog(SecondCatalogEntity entity);

	/**
	 * saveFirstCatalog:保存二级分类. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void saveFirstCatalog(FirstCatalogEntity entity);

	/**
	 * saveThirdCatalog:保存三级分类. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void saveThirdCatalog(ThirdCatalogEntity entity);

	/**  
	 * queryFirstAll:查询一级分类集合. <br/>  
	 *  
	 * @author hebin  
	 * @return  
	 * @since JDK 1.7  
	 */
	List<FirstCatalogEntity> queryFirstAll();

	/**  
	 * querySecondByFirstId:根据一级分类编号查询二级分类集合. <br/>  
	 *  
	 * @author hebin  
	 * @param firstId
	 * @return  
	 * @since JDK 1.7  
	 */
	List<SecondCatalogEntity> querySecondByFirstId(String firstId);

	/**  
	 * queryThirdBySecondId:根据二级分类编号查询三级分类集合. <br/>  
	 *  
	 * @author hebin  
	 * @param secondId
	 * @return  
	 * @since JDK 1.7  
	 */
	List<ThirdCatalogEntity> queryThirdBySecondId(String secondId);

	/**  
	 * delete:删除. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @param type  
	 * @since JDK 1.7  
	 */
	void delete(String id, String type) throws Exception ;

	/**  
	 * modifyFirstCatalog:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void modifyFirstCatalog(FirstCatalogEntity entity);

	/**  
	 * modifySecondCatalog:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void modifySecondCatalog(SecondCatalogEntity entity);

	/**  
	 * modifyThirdCatalog:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void modifyThirdCatalog(ThirdCatalogEntity entity);

	/**  
	 * queryFirstAll:查询一级分类集合. <br/>  
	 *  
	 * @author hebin  
	 * @return  
	 * @since JDK 1.7  
	 */
	List<SecondCatalogEntity> querySecondAll();

	/**  
	 * queryFirstAll:查询一级分类集合. <br/>  
	 *  
	 * @author hebin  
	 * @return  
	 * @since JDK 1.7  
	 */
	List<ThirdCatalogEntity> queryThirdAll();

}
