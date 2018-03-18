package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;

@Component
public class CalCapitalPoolTimeTaskJob implements Job{

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		orderFeignClient.calCapitalPool(Constants.FIRST_VERSION);
	}
}
