package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.ThirdFeignClient;

@Component
public class PageViewTimeTaskJob implements Job{

	@Resource
	ThirdFeignClient thirdFeignClient;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		thirdFeignClient.persistTask(Constants.FIRST_VERSION);
	}

}
