package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;

@Component
public class EndActiveTimeTaskJob implements Job {

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Integer centerId = (Integer) jobDataMap.get("centerId");
		Integer activeId = (Integer) jobDataMap.get("activeId");
		goodsFeignClient.endActive(Constants.FIRST_VERSION, activeId, centerId);
	}
}
