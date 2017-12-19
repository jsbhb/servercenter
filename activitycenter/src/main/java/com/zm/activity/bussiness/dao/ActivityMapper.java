package com.zm.activity.bussiness.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zm.activity.pojo.Activity;

public interface ActivityMapper {

	List<Activity> listActivity(@Param("centerId")Integer centerId);

}
