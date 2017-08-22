package com.zm.log.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.log.pojo.LogInfo;

public interface LogMapper {

	void saveLogInfo(LogInfo info);
	
	List<LogInfo> listLogInfo(Map<String,Object> param);
	
	void removeLogInfo(String endTime);
}
