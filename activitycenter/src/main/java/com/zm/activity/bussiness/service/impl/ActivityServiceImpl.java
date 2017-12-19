package com.zm.activity.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.activity.bussiness.dao.ActivityMapper;
import com.zm.activity.bussiness.service.ActivityService;
import com.zm.activity.pojo.Activity;

@Service
public class ActivityServiceImpl implements ActivityService{

	@Resource
	ActivityMapper activityMapper;
	
	@Override
	public List<Activity> listActivity(Integer centerId) {
		
		return activityMapper.listActivity(centerId);
	}

}
