package com.zm.timetask.bussiness.job;

import java.util.ArrayList;
import java.util.List;

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
import com.zm.timetask.feignclient.ActivityFeignClient;
import com.zm.timetask.pojo.ResultModel;

@Component
public class StartGivenCouponTaskJob implements Job {

	@Resource
	ActivityFeignClient activityFeignClient;
	
	@Resource
	private Scheduler scheduler;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Integer centerId = (Integer) jobDataMap.get("centerId");
		String couponId = (String) jobDataMap.get("id");
		String endTime = (String) jobDataMap.get("endTime");

		List<String> couponIds = new ArrayList<String>();
		couponIds.add(couponId);
		ResultModel result = activityFeignClient.giveOutCoupon(Constants.FIRST_VERSION, centerId, couponIds);
		if (result.isSuccess()) {
			if (endTime != null) {
				
			}
			//移除开始定时器
			TriggerKey startTriggerKey = TriggerKey.triggerKey(centerId + "start" + couponId+0);
			CronTrigger startTrigger;
			try {
				startTrigger = (CronTrigger) scheduler.getTrigger(startTriggerKey);
				if (startTrigger != null) {
					JobKey startJobKey = JobKey.jobKey(centerId + "start" + couponId+0);
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

}
