package com.zm.user.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.user.common.ResultModel;
import com.zm.user.pojo.FuzzySearchGrade;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ShopEntity;
import com.zm.user.pojo.po.GradeTypePO;

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
	
	ShopEntity queryByGradeId(Integer gradeId);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateShop(ShopEntity entity);

	/**
	 * @fun 模糊搜索等级
	 * @param entity
	 * @return
	 */
	ResultModel fuzzySearch(FuzzySearchGrade entity);

	/**
	 * @fun 新增客户类型
	 * @param entity
	 * @return
	 */
	ResultModel saveGradeType(GradeTypePO entity);

	/**
	 * @fun 获取客户类型（ID为null获取所有，ID有的获取本身及所有父级）
	 * @param id
	 * @return
	 */
	ResultModel listGradeType(Integer id);

	/**
	 * @fun 获取他的下一级
	 * @param id
	 * @return
	 */
	ResultModel listGradeTypeChildren(Integer id);

	ResultModel removeGradeType(Integer id);

	ResultModel updateGradeType(GradeTypePO entity);

	ResultModel getGradeType(Integer id);

	ResultModel initAreaCenter(Integer id);
}
