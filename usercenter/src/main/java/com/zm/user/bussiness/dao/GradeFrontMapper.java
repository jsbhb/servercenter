package com.zm.user.bussiness.dao;

import com.zm.user.pojo.Grade;
import com.zm.user.pojo.GradeConfig;
import com.zm.user.pojo.GradeFront;

public interface GradeFrontMapper {

	GradeConfig getGradeConfig(Integer gradeId);

	String getClientUrlById(Integer id);

	String getMobileUrl(Integer parentId);

	Grade getGradeUrl(Integer id);

	void saveGradeFront(GradeFront grade);

	GradeFront getDataByPhone(String phone);

	void applyShopResubmit(GradeFront gradeFront);
}
