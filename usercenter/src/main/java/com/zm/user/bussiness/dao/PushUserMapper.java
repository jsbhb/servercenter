package com.zm.user.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.user.pojo.PushUser;

public interface PushUserMapper {

	void savePushUser(PushUser pushUser);
	
	PushUser getPushUserById(Integer id);
	
	void updatePushUserStatus(Map<String,Object> param);

	int countShopPassPushUser(Integer gradeId);

	List<PushUser> listPushUserByGradeId(Integer gradeId);

	PushUser verify(Map<String, Object> param);

	List<PushUser> listBindingShop(Integer userId);

	void updateRepayingPush(Integer id);

	List<PushUser> listPassPushUserByGradeId(Integer shopId);

	Integer verifyEffective(Map<String, Object> param);
}
