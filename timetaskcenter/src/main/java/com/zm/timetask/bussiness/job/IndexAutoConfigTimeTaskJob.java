package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;

@Component
public class IndexAutoConfigTimeTaskJob implements Job{
	
	@Resource
	GoodsFeignClient goodsFeignClient;
	
	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		goodsFeignClient.indexAutoConfig(Constants.FIRST_VERSION);
	}
}
