package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;

@Component
public class CacheDayTimeTaskJob implements Job{

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		orderFeignClient.saveDayCacheToWeek(Constants.FIRST_VERSION);
	}
}
