package com.zm.timetask.base;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.timetask.bussiness.service.TimeTaskService;

@Component
public class SysInit {

	@Resource
	TimeTaskService timeTaskService;
	
	@PostConstruct
	public void init(){
		
		timeTaskService.runAllTimeTask();
	}
	
}
