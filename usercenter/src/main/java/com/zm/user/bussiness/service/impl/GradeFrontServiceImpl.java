package com.zm.user.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.user.bussiness.dao.GradeFrontMapper;
import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.pojo.GradeConfig;

@Service
public class GradeFrontServiceImpl implements GradeFrontService {

	@Resource
	GradeFrontMapper gradeFrontMapper;
	
	@Override
	public GradeConfig getGradeConfig(Integer gradeId) {
		
		return gradeFrontMapper.getGradeConfig(gradeId);
	}

	@Override
	public String getClientUrl(Integer centerId) {
		return gradeFrontMapper.getClientUrlById(centerId);
	}

}
