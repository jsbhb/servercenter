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
import com.zm.goods.pojo.SpecsEntity;
import com.zm.goods.pojo.SpecsTemplateEntity;
import com.zm.goods.pojo.SpecsValueEntity;

/**
 * ClassName: SpecsService <br/>
 * Function: 规格服务类. <br/>
 * date: Nov 9, 2017 8:37:03 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface SpecsService {

	/**
	 * queryByPage:分页查询规格信息. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<SpecsTemplateEntity> queryByPage(SpecsTemplateEntity entity);

	/**
	 * queryById:根据编号查询规格. <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	SpecsTemplateEntity queryById(int id);

	/**
	 * save:保存规格模板. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void save(SpecsTemplateEntity entity);

	/**
	 * queryAll:查询所有规格. <br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	List<SpecsTemplateEntity> queryAll();

	/**  
	 * saveSpecs:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void saveSpecs(SpecsEntity entity);

	/**  
	 * saveValue:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param value  
	 * @since JDK 1.7  
	 */
	void saveValue(SpecsValueEntity value);

}
