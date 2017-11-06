/**  
 * Project Name:usercenter  
 * File Name:GradeMapper.java  
 * Package Name:com.zm.user.bussiness.dao  
 * Date:Oct 29, 20178:04:02 PM  
 *  
 */
package com.zm.user.bussiness.dao;

import com.github.pagehelper.Page;
import com.zm.user.pojo.Grade;

/**
 * ClassName: GradeMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Oct 29, 2017 8:04:02 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface GradeMapper<T> {

	/**
	 * 
	 * selectForPage:分页查询分级. <br/>  
	 *  
	 * @author hebin  
	 * @param grade
	 * @return  
	 * @since JDK 1.7
	 */
	Page<T> selectForPage(Grade grade);
	
	/**
	 * 
	 * selectById:根据编号查询. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7
	 */
	Grade selectById(int id);

}
