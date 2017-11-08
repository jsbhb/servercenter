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
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.service.GradeService;
import com.zm.user.pojo.Grade;

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
@Transactional
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

}
