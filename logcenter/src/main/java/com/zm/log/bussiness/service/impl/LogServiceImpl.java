package com.zm.log.bussiness.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.log.bussiness.dao.LogMapper;
import com.zm.log.bussiness.service.LogService;
import com.zm.log.pojo.LogInfo;

@Service
public class LogServiceImpl implements LogService{

	@Resource
	LogMapper logMapper;
	
	
	@Override
	public void saveLog(LogInfo info) {
		logMapper.saveLogInfo(info);
	}

	@Override
	public List<LogInfo> listLogInfo(Map<String, Object> param) {
		return logMapper.listLogInfo(param);
	}

	@Override
	public void removeLogInfo(String endTime) {
		logMapper.removeLogInfo(endTime);
	}

}
