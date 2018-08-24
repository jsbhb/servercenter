/**  
 * Project Name:usercenter  
 * File Name:GradeMapper.java  
 * Package Name:com.zm.user.bussiness.dao  
 * Date:Oct 29, 20178:04:02 PM  
 *  
 */
package com.zm.user.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.user.pojo.FuzzySearchGrade;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ShopEntity;
import com.zm.user.pojo.po.GradeTypePO;
import com.zm.user.pojo.po.RebateFormula;

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

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void update(Grade entity);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateGradeData(Grade entity);
	
	/**
	 * 
	 * selectById:根据编号查询. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7
	 */
	ShopEntity selectByGradeId(int gradeId);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void updateGradeConfig(ShopEntity entity);

	/**  
	 * update:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void insertGradeConfig(ShopEntity entity);

	List<FuzzySearchGrade> fuzzyListGrade(FuzzySearchGrade entity);

	/**
	 * @fun 新增客户类型
	 * @param entity
	 */
	void saveGradeType(GradeTypePO entity);

	/**
	 * @fun 获取客户类型
	 * @param id
	 * @return
	 */
	List<GradeTypePO> listParentGradeTypeById(Integer id);
	
	/**
	 * @fun 获取客户类型
	 * @param id
	 * @return
	 */
	List<GradeTypePO> listGradeType();

	/**
	 * @fun 获取客户类型的子类型
	 * @param id
	 * @return
	 */
	String listGradeTypeChildren(Integer id);
	
	int countGradeByGradeType(Integer id);

	void removeGradeType(Integer id);

	void updateGradeType(GradeTypePO entity);
	
	List<Grade> listParentGradeById(Integer id);

	List<Grade> listGrade();

	String listChildrenGrade(Integer gradeId);

	GradeTypePO getGradeType(Integer id);

	List<GradeTypePO> listGradeTypeByIds(List<Integer> list);

	List<GradeTypePO> listGradeTypeChildrenById(Integer id);
	
	Grade getGradeForInitAreaCenterById(Integer id);
	
	void updateGradeInit(Integer id);
	
	List<Grade> listButtjointGrade();

	void saveGradeTypeRebateFormula(RebateFormula rebateFormula);

	void updateGradeTypeRebateFormula(RebateFormula rebateFormula);

	Page<RebateFormula> listGradeTypeRebateFormula(RebateFormula rebateFormula);

	RebateFormula getGradeTypeRebateFormulaById(Integer id);

	Integer getIdByGradeTypeId(Integer gradeTypeId);

	List<RebateFormula> listAllGradeTypeRebateFormula();

}
