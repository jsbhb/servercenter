package com.zm.timetask.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.timetask.bussiness.dao.TimeTaskMapper;
import com.zm.timetask.bussiness.service.TimeTaskService;
import com.zm.timetask.constants.Constants;
import com.zm.timetask.pojo.TimeTaskModel;
import com.zm.timetask.util.SpringContextUtil;

@Service
// @PostConstruct先于springContextUtil执行，该标签强制初始化springContextUtil类
@DependsOn("springContextUtil")
@Transactional(isolation=Isolation.READ_COMMITTED)
public class TimeTaskServiceImpl implements TimeTaskService {

	@Resource
	TimeTaskMapper timeTaskMapper;

	@Resource
	private Scheduler scheduler;

	@Override
	public void runAllTimeTask() {

		List<TimeTaskModel> modelList = timeTaskMapper.listTimeTask();

		if (modelList != null) {
			for (TimeTaskModel model : modelList) {
				Integer status = model.getStatus();

				if (Constants.TIMETASK_ENABLE.equals(status)) {
					startTimeTask(model);
				}
				if (Constants.TIMETASK_DISABLE.equals(status)) {
					stopTimeTask(model);
				}
			}
		}
	}

	@Override
	public void stopTimeTask(Integer id) {

		TimeTaskModel model = timeTaskMapper.getTimeTaskById(id);

		stopTimeTask(model);

		timeTaskMapper.stopTimeTask(id);

	}

	private void stopTimeTask(TimeTaskModel model) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(model.getJobName(), model.getJobGroup());
			if (triggerKey != null) {
				scheduler.pauseTrigger(triggerKey);// 停止触发器
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("停止任务出错");
		}
	}

	@Override
	public void startTimeTask(Integer id) {

		TimeTaskModel model = timeTaskMapper.getTimeTaskById(id);

		startTimeTask(model);

		timeTaskMapper.startTimeTask(id);
	}

	@Override
	public void updateTimeTask(TimeTaskModel model) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(model.getJobName(), model.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger != null) {
				String searchCron = model.getCronExpression();
				String currentCron = trigger.getCronExpression();
				if (!searchCron.equals(currentCron)) {
					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);

					// 按新的cronExpression表达式重新构建trigger
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
							.build();

					// 按新的trigger重新设置job执行
					scheduler.rescheduleJob(triggerKey, trigger);
				}
			}

			timeTaskMapper.updateTimeTask(model);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("更新任务出错");
		}

	}

	@Override
	public void saveTimeTask(TimeTaskModel model) {

		if (Constants.TIMETASK_ENABLE.equals(model.getStatus())) {
			startTimeTask(model);
		}
		timeTaskMapper.saveTimeTask(model);
	}

	@SuppressWarnings("unchecked")
	private void startTimeTask(TimeTaskModel model) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(model.getJobName(), model.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger == null) {

				JobDetail jobDetail = JobBuilder
						.newJob((Class<? extends Job>) SpringContextUtil.getBean(model.getTargetObject()).getClass())
						.withIdentity(model.getJobName(), model.getJobGroup()).build();

				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(model.getCronExpression());

				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(model.getJobName(), model.getJobGroup())
						.withSchedule(scheduleBuilder).build();

				// 把trigger和jobDetail注入到调度器
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				String searchCron = model.getCronExpression();
				String currentCron = trigger.getCronExpression();
				if (!searchCron.equals(currentCron)) {
					// 表达式调度构建器
					CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);

					// 按新的cronExpression表达式重新构建trigger
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
							.build();
				}
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("开始任务出错");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dynamicSchedule(Integer centerId, String id, String startTime, String endTime, Integer type) {
		String name = centerId + "start" + id + type;
		JobDetail jobDetail = null;

		jobDetail = JobBuilder.newJob((Class<? extends Job>) SpringContextUtil.getBean(mapping.get(type)).getClass())
				.withIdentity(name, name).build();

		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("centerId", centerId);
		jobDataMap.put("id", id);
		jobDataMap.put("endTime", endTime);

		// 表达式调度构建器
		String[] timeArr = startTime.split(" ");
		String[] frontTimeArr = timeArr[0].split("-");
		String[] afterTimeArr = timeArr[1].split(":");
		String cron = afterTimeArr[2] + " " + afterTimeArr[1] + " " + afterTimeArr[0] + " " + frontTimeArr[2] + " "
				+ frontTimeArr[1] + " ? " + frontTimeArr[0];
		System.out.println(cron);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, name).withSchedule(scheduleBuilder)
				.build();

		// 把trigger和jobDetail注入到调度器
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new RuntimeException("加入定时器失败");
		}

	}

	private static Map<Integer, String> mapping = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "startActiveTimeTaskJob");
			put(1, "startGivenCouponTaskJob");
		}
	};
	
	@Override
	public Page<TimeTaskModel> queryAllTimeTask(TimeTaskModel entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return timeTaskMapper.queryListTimeTask(entity);
	}

	@Override
	public TimeTaskModel queryTimeTaskById(Integer id) {
		TimeTaskModel model = timeTaskMapper.getTimeTaskById(id);
		return model;
	}
}
