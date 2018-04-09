package com.zm.user.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.user.bussiness.dao.GradeFrontMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.pojo.GradeConfig;
import com.zm.user.pojo.UserInfo;

@Service
public class GradeFrontServiceImpl implements GradeFrontService {

	@Resource
	GradeFrontMapper gradeFrontMapper;
	
	@Resource
	UserMapper userMapper;
	
	@Override
	public GradeConfig getGradeConfig(Integer centerId, Integer shopId, Integer userId) {
		if(shopId != null){
			Integer parentId = userMapper.getParentIdByGradeId(shopId);
			if(!centerId.equals(parentId)){
				return null;
			}
			return gradeFrontMapper.getGradeConfig(shopId);
		}
		if(userId != null){
			UserInfo user = userMapper.getUserInfo(userId);
			if(!centerId.equals(user.getCenterId())){
				return null;
			}
			if(user.getShopId() == null){
				return null;
			}
			return gradeFrontMapper.getGradeConfig(user.getShopId());
		}
		return null;
	}

	@Override
	public String getClientUrl(Integer centerId) {
		return gradeFrontMapper.getClientUrlById(centerId);
	}

	@Override
	public String getMobileUrl(Integer shopId) {
		Integer parentId = userMapper.getParentIdByGradeId(shopId);
		
		return gradeFrontMapper.getMobileUrl(parentId);
	}

}
