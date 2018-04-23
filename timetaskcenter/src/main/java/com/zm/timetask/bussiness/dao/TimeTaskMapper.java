package com.zm.timetask.bussiness.dao;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.timetask.pojo.TimeTaskModel;

public interface TimeTaskMapper {

	List<TimeTaskModel> listTimeTask();
	
	TimeTaskModel getTimeTaskById(Integer id);
	
	void saveTimeTask(TimeTaskModel model);
	
	void stopTimeTask(Integer id);
	
	void startTimeTask(Integer id);
	
	void updateTimeTask(TimeTaskModel model);
	
	Page<TimeTaskModel> queryListTimeTask(TimeTaskModel model);
}
