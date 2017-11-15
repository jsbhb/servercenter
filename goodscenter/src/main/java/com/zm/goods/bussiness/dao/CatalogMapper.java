/**  
 * Project Name:goodscenter  
 * File Name:BrandMapper.java  
 * Package Name:com.zm.goods.bussiness.dao  
 * Date:Nov 9, 20178:48:41 PM  
 *  
 */
package com.zm.goods.bussiness.dao;

import java.util.List;

import com.zm.goods.pojo.FirstCatalogEntity;
import com.zm.goods.pojo.SecondCatalogEntity;
import com.zm.goods.pojo.ThirdCatalogEntity;

/**
 * ClassName: BrandMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Nov 9, 2017 8:48:41 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface CatalogMapper {

	/**
	 * selectAll:查询所有分类. <br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	List<FirstCatalogEntity> selectAll();

	/**
	 * 
	 * insertFristCatalog:插入一级分类. <br/>
	 * 
	 * @author hebin
	 * @param log
	 * @since JDK 1.7
	 */
	void insertFristCatalog(FirstCatalogEntity entity);

	/**
	 * 
	 * insertSecondCatalog:插入二级分类. <br/>
	 * 
	 * @author hebin
	 * @param log
	 * @since JDK 1.7
	 */
	void insertSecondCatalog(SecondCatalogEntity entity);

	/**
	 * 
	 * insertThirdCatalog:插入三级分类. <br/>
	 * 
	 * @author hebin
	 * @param log
	 * @since JDK 1.7
	 */
	void insertThirdCatalog(ThirdCatalogEntity entity);

	/**  
	 * selectFirstAll:检索所有一级分类. <br/>  
	 *  
	 * @author hebin  
	 * @return  
	 * @since JDK 1.7  
	 */
	List<FirstCatalogEntity> selectFirstAll();

	/**  
	 * selectSecondByFirstId:根据一级分类编号检索二级分类. <br/>  
	 *  
	 * @author hebin  
	 * @param firstId
	 * @return  
	 * @since JDK 1.7  
	 */
	List<SecondCatalogEntity> selectSecondByFirstId(String firstId);

	/**  
	 * selectThirdBySecondId:根据二级分类编号检索三级分类. <br/>  
	 *  
	 * @author hebin  
	 * @param secondId
	 * @return  
	 * @since JDK 1.7  
	 */
	List<ThirdCatalogEntity> selectThirdBySecondId(String secondId);

}
