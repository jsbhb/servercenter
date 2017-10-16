package com.zm.timetask.bussiness.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;
import com.zm.timetask.pojo.ResultModel;

@Component
public class ActivityTimeTaskJob {

	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	Scheduler scheduler;

	@SuppressWarnings("unchecked")
	public void removeActiveTimeTask() throws Exception {
		ResultModel resultModel = goodsFeignClient.getEndActive(Constants.FIRST_VERSION);
		if (resultModel.isSuccess()) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) resultModel.getObj();
			for (Map<String, Object> map : list) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					List<Integer> activeIdList = (List<Integer>) entry.getValue();
					if (activeIdList != null && activeIdList.size() > 0) {
						for (Integer activeId : activeIdList) {
							String startName = entry.getKey() + "start" + activeId;
							String endName = entry.getKey() + "end" + activeId;
							TriggerKey startTriggerKey = TriggerKey.triggerKey(startName);
							CronTrigger startTrigger = (CronTrigger) scheduler.getTrigger(startTriggerKey);
							TriggerKey endTriggerKey = TriggerKey.triggerKey(endName);
							CronTrigger endTrigger = (CronTrigger) scheduler.getTrigger(endTriggerKey);
							if (endTrigger != null) {
								JobKey endJobKey = JobKey.jobKey(endName);
								scheduler.pauseTrigger(endTriggerKey);// 停止触发器
								scheduler.unscheduleJob(endTriggerKey);// 移除触发器
								scheduler.deleteJob(endJobKey);// 移除任务
							}
							if (startTrigger != null) {
								JobKey startJobKey = JobKey.jobKey(startName);
								scheduler.pauseTrigger(startTriggerKey);// 停止触发器
								scheduler.unscheduleJob(startTriggerKey);// 移除触发器
								scheduler.deleteJob(startJobKey);// 移除任务
							}
						}
					}
				}
			}
		}
	}
}
