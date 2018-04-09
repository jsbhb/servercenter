package com.zm.user.bussiness.service;

import com.zm.user.pojo.GradeConfig;

public interface GradeFrontService {

	GradeConfig getGradeConfig(Integer centerId, Integer shopId, Integer userId);

	String getClientUrl(Integer centerId);

	String getMobileUrl(Integer shopId);
}
