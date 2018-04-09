package com.zm.log.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.log.pojo.ExceptionLog;
import com.zm.log.pojo.LogInfo;
import com.zm.log.pojo.OpenInfLog;

public interface LogMapper {

	void saveExceptionLog(ExceptionLog info);
	
	List<ExceptionLog> listLogInfo(Map<String,Object> param);
	
	void removeLogInfo(String endTime);
	
	void saveOpenInfLog(OpenInfLog log);

	void saveLogInfo(LogInfo logInfo);
}
