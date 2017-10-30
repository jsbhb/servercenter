package com.zm.user.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.user.common.Pagination;
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
	Page<Grade> queryForPagination(Pagination pagination,Grade grade);
}
