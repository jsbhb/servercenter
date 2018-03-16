package com.zm.user.bussiness.service;

import com.zm.user.common.ResultModel;
import com.zm.user.pojo.PushUser;

public interface PushUserService {

	ResultModel savePushUser(PushUser pushUser);

	ResultModel pushUserAudit(boolean pass, Integer id);

	ResultModel listPushUserByGradeId(Integer gradeId);

	ResultModel getPushUserById(Integer id);

	ResultModel pushUserVerify(String phone, Integer gradeId);

	ResultModel listBindingShop(Integer userId);

	ResultModel repayingPush(Integer id);

	ResultModel pushUserOrderCount(Integer shopId);

	boolean verifyEffective(Integer shopId, Integer pushUserId);

	ResultModel listAllPushUser();
}
