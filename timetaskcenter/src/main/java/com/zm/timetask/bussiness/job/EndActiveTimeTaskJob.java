package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;

@Component
public class EndActiveTimeTaskJob implements Job {

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	private Scheduler scheduler;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Integer centerId = (Integer) jobDataMap.get("centerId");
		Integer activeId = (Integer) jobDataMap.get("activeId");
		goodsFeignClient.endActive(Constants.FIRST_VERSION, activeId, centerId);
		// 移除开始定时器
		TriggerKey startTriggerKey = TriggerKey.triggerKey(centerId + "end" + activeId + 0);
		CronTrigger startTrigger;
		try {
			startTrigger = (CronTrigger) scheduler.getTrigger(startTriggerKey);
			if (startTrigger != null) {
				JobKey startJobKey = JobKey.jobKey(centerId + "end" + activeId + 0);
				scheduler.pauseTrigger(startTriggerKey);// 停止触发器
				scheduler.unscheduleJob(startTriggerKey);// 移除触发器
				scheduler.deleteJob(startJobKey);// 移除任务
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
