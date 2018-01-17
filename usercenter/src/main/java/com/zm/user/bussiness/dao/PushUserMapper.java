package com.zm.user.bussiness.dao;

import java.util.Map;

import com.zm.user.pojo.PushUser;

public interface PushUserMapper {

	void savePushUser(PushUser pushUser);
	
	PushUser getPushUserById(Integer id);
	
	void updatePushUserStatus(Map<String,Object> param);
}
