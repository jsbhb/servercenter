package com.zm.user.bussiness.service;

import com.zm.user.pojo.PushUser;

public interface PushUserService {

	void savePushUser(PushUser pushUser);

	Integer pushUserAudit(boolean pass, Integer id);
}
