package com.zm.log.bussiness.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.log.bussiness.dao.LogMapper;
import com.zm.log.bussiness.service.LogService;
import com.zm.log.pojo.ExceptionLog;
import com.zm.log.pojo.LogInfo;
import com.zm.log.pojo.OpenInfLog;

@Service
@Transactional(isolation=Isolation.READ_COMMITTED)
public class LogServiceImpl implements LogService{

	@Resource
	LogMapper logMapper;
	
	
	@Override
	public void saveExceptionLog(ExceptionLog info) {
		logMapper.saveExceptionLog(info);
	}

	@Override
	public List<ExceptionLog> listLogInfo(Map<String, Object> param) {
		return logMapper.listLogInfo(param);
	}

	@Override
	public void removeLogInfo(String endTime) {
		logMapper.removeLogInfo(endTime);
	}

	@Override
	public void saveOpenInfoLog(OpenInfLog log) {
		logMapper.saveOpenInfLog(log);
	}

	@Override
	public void saveLogInfo(LogInfo logInfo) {
		logMapper.saveLogInfo(logInfo);
	}

}
