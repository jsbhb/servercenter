package com.zm.user.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.user.bussiness.component.UserComponent;
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
	
	@Resource
	UserComponent userComponent;
	
	@Override
	public GradeConfig getGradeConfig(Integer mallId, Integer shopId, Integer userId) {
		if(shopId != null){
			Integer tmallId = userComponent.getMallId(shopId);
			if(!mallId.equals(tmallId)){
				return null;
			}
			return gradeFrontMapper.getGradeConfig(shopId);
		}
		if(userId != null){
			UserInfo user = userMapper.getUserInfo(userId);
			if(!mallId.equals(user.getCenterId())){
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
	public String getClientUrl(Integer mallId) {
		return gradeFrontMapper.getClientUrlById(mallId);
	}

	@Override
	public String getMobileUrl(Integer shopId) {
		Integer parentId = userMapper.getParentIdByGradeId(shopId);
		
		return gradeFrontMapper.getMobileUrl(parentId);
	}

}
