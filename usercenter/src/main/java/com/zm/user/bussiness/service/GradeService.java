package com.zm.user.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.user.pojo.Grade;

/**
 * 
 * ClassName: GradeService <br/>  
 * Function: 分级服务. <br/>   
 * date: Oct 29, 2017 7:56:56 PM <br/>  
 *  
 * @author hebin  
 * @version   
 * @since JDK 1.7
 */
public interface GradeService {

	/**
	 * 
	 * queryForPagination:分页查询分级信息. <br/>  
	 *  
	 * @author hebin  
	 * @param grade
	 * @return  
	 * @since JDK 1.7
	 */
	Page<Grade> queryForPagination(Grade grade);

	/**  
	 * queryById:根据编号查询. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	Grade queryById(Integer id);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void update(Grade entity);
}
