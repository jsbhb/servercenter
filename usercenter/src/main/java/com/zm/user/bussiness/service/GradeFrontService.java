package com.zm.user.bussiness.service;

import com.zm.user.pojo.GradeConfig;

public interface GradeFrontService {

	GradeConfig getGradeConfig(Integer gradeId);

	String getClientUrl(Integer centerId);
}
