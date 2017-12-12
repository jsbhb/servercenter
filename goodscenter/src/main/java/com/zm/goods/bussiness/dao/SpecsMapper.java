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
import com.zm.goods.pojo.SpecsEntity;
import com.zm.goods.pojo.SpecsTemplateEntity;
import com.zm.goods.pojo.SpecsValueEntity;

/**
 * ClassName: SpecsMapper <br/>
 * Function: 规格持久化实体. <br/>
 * date: Nov 9, 2017 8:48:41 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface SpecsMapper {

	/**
	 * selectForPage:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @return
	 * @since JDK 1.7
	 */
	Page<SpecsTemplateEntity> selectForPage(SpecsTemplateEntity entity);

	/**
	 * selectById:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	SpecsTemplateEntity selectById(int id);

	/**
	 * insertTemplate:插入模板. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insertTemplate(SpecsTemplateEntity entity);

	/**
	 * insertSpecs:批量插入规格. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insertSpces(List<SpecsEntity> list);
	
	/**
	 * insertSpecs:插入规格. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insertSpce(SpecsEntity entity);

	/**
	 * insertSpcesValue:批量插入规格值. <br/>
	 * 
	 * @author hebin
	 * @param entity
	 * @since JDK 1.7
	 */
	void insertSpcesValue(List<SpecsValueEntity> list);

	/**
	 * selectAll:检索所有品牌. <br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	List<SpecsTemplateEntity> selectAll();

}
