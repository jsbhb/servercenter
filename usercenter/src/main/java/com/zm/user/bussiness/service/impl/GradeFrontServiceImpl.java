package com.zm.user.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.user.bussiness.component.UserComponent;
import com.zm.user.bussiness.dao.GradeFrontMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.pojo.Grade;
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
		GradeConfig config = null;
		if(shopId != null){
			Integer tmallId = userComponent.getMallId(shopId);
			if(!mallId.equals(tmallId)){
				return null;
			}
			config = gradeFrontMapper.getGradeConfig(shopId);
			if(config == null){
				config = new GradeConfig();
			}
			Grade grade = userComponent.getUrl(shopId);
			config.setUrl(grade.getRedirectUrl());
			config.setMobileUrl(grade.getMobileUrl());
			return config;
		}
		if(userId != null){
			UserInfo user = userMapper.getUserInfo(userId);
			if(!mallId.equals(user.getCenterId())){
				return null;
			}
			if(user.getShopId() == null){
				return null;
			}
			config = gradeFrontMapper.getGradeConfig(user.getShopId());
			if(config == null){
				config = new GradeConfig();
			}
			Grade grade = userComponent.getUrl(shopId);
			config.setUrl(grade.getRedirectUrl());
			config.setMobileUrl(grade.getMobileUrl());
			return config;
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
