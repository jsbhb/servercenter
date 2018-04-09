/**  
 * Project Name:usercenter  
 * File Name:GradeServiceImpl.java  
 * Package Name:com.zm.user.bussiness.service.impl  
 * Date:Oct 29, 20177:58:30 PM  
 *  
 */
package com.zm.user.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.service.GradeService;
import com.zm.user.common.ResultModel;
import com.zm.user.pojo.FuzzySearchGrade;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ShopEntity;

/**
 * ClassName: GradeServiceImpl <br/>
 * Function: 分级管理实现类. <br/>
 * date: Oct 29, 2017 7:58:30 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
@Transactional(isolation=Isolation.READ_COMMITTED)
public class GradeServiceImpl implements GradeService {

	@Resource
	GradeMapper<Grade> gradeMapper;

	@Override
	public Page<Grade> queryForPagination(Grade grade) {
		PageHelper.startPage(grade.getCurrentPage(), grade.getNumPerPage(), true);
		return gradeMapper.selectForPage(grade);
	}

	@Override
	public Grade queryById(Integer id) {
		return gradeMapper.selectById(id);
	}

	@Override
	public void update(Grade entity) {
		gradeMapper.update(entity);
		gradeMapper.updateGradeData(entity);
	}

	@Override
	public ShopEntity queryByGradeId(Integer gradeId) {
		return gradeMapper.selectByGradeId(gradeId);
	}

	@Override
	public void updateShop(ShopEntity entity) {
		ShopEntity shopEntity = gradeMapper.selectByGradeId(entity.getGradeId());
		if (shopEntity != null) {
			gradeMapper.updateGradeConfig(entity);
		} else {
			gradeMapper.insertGradeConfig(entity);
		}
	}

	@Override
	public ResultModel fuzzySearch(FuzzySearchGrade entity) {
		
		return new ResultModel(true, gradeMapper.fuzzyListGrade(entity));
	}

}
