package com.zm.user.bussiness.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.user.bussiness.dao.PushUserMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.PushUserService;
import com.zm.user.constants.Constants;
import com.zm.user.pojo.PushUser;
import com.zm.user.pojo.UserInfo;

@Service
public class PushUserServiceImpl implements PushUserService {

	@Resource
	PushUserMapper pushUserMapper;
	
	@Resource
	UserMapper userMapper;
	
	@Override
	public void savePushUser(PushUser pushUser) {
		
		pushUserMapper.savePushUser(pushUser);
	}

	@Override
	public Integer pushUserAudit(boolean pass, Integer id) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", id);
		if(pass){
			param.put("status", Constants.AUDIT_PASS);
			pushUserMapper.updatePushUserStatus(param);
			PushUser pushUser = pushUserMapper.getPushUserById(id);
			UserInfo userInfo = new UserInfo();
			userInfo.setPhone(pushUser.getPhone());
			Integer userId = userMapper.getUserIdByUserInfo(userInfo);
			if(userId == null){
				Integer parentId = userMapper.getParentIdByGradeId(pushUser.getGradeId());
				userInfo.setCenterId(parentId);
				userInfo.setShopId(pushUser.getGradeId());
				userInfo.setPhoneValidate(1);
				userInfo.setStatus(1);
				userMapper.saveUser(userInfo);
				return userInfo.getId();
			} else {
				return userId;
			}
		} else {
			param.put("status", Constants.AUDIT_UN_PASS);
			pushUserMapper.updatePushUserStatus(param);
		}
		return null;
	}

}
