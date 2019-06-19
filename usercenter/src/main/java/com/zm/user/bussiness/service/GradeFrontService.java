package com.zm.user.bussiness.service;

import com.zm.user.common.ResultModel;
import com.zm.user.pojo.GradeConfig;
import com.zm.user.pojo.GradeFront;

public interface GradeFrontService {

	GradeConfig getGradeConfig(Integer centerId, Integer shopId, Integer userId);

	String getClientUrl(Integer centerId);

	String getMobileUrl(Integer shopId);
	
	byte[] getShopBillboard(Integer shopId);

	ResultModel applyShop(GradeFront grade);

	ResultModel applyShopCheck(String phone);

	ResultModel getDataByPhone(String phone);

	ResultModel applyShopResubmit(GradeFront gradeFront);
}
