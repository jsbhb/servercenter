package com.zm.activity.bussiness.service;

import java.util.List;

import com.zm.activity.pojo.Activity;

public interface ActivityService {

	List<Activity> listActivity(Integer centerId);
}
