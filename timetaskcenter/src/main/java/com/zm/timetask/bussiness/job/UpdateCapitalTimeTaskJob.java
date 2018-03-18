package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.FinanceFeignClient;

@Component
public class UpdateCapitalTimeTaskJob implements Job{
	
	@Resource
	FinanceFeignClient financeFeignClient;
	
	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		financeFeignClient.updateCapitalPoolTask(Constants.FIRST_VERSION);
	}
}
