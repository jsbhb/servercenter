package com.zm.timetask.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.timetask.bussiness.dao.TimeTaskMapper;
import com.zm.timetask.bussiness.job.OrderTimeTaskJob;
import com.zm.timetask.bussiness.service.TimeTaskService;
import com.zm.timetask.constants.Constants;
import com.zm.timetask.pojo.TimeTaskModel;
import com.zm.timetask.util.SpringContextUtil;

@Service
// @PostConstruct先于springContextUtil执行，该标签强制初始化springContextUtil类
@DependsOn("springContextUtil")
@Transactional
public class TimeTaskServiceImpl implements TimeTaskService {

	@Resource
	TimeTaskMapper timeTaskMapper;

	@Resource
	private Scheduler scheduler;

	@Resource
	OrderTimeTaskJob orderTimeTaskJob;

	@Override
	public void runAllTimeTask() {

		List<TimeTaskModel> modelList = timeTaskMapper.listTimeTask();
		MethodInvokingJobDetailFactoryBean methodInvJobDetailFB = null;
		try {

			if (modelList != null) {
				for (TimeTaskModel model : modelList) {
					Integer status = model.getStatus();
					TriggerKey triggerKey = TriggerKey.triggerKey(model.getJobName(), model.getJobGroup());
					CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
					// 还没有添加到scheduler
					if (trigger == null) {
						if (Constants.TIMETASK_DISABLE.equals(status)) {
							continue;
						}
						methodInvJobDetailFB = new MethodInvokingJobDetailFactoryBean();
						methodInvJobDetailFB.setName(model.getJobName());
						methodInvJobDetailFB.setGroup(model.getJobGroup());
						methodInvJobDetailFB.setTargetObject(SpringContextUtil.getBean(model.getTargetObject()));
						// 设置任务方法
						methodInvJobDetailFB.setTargetMethod(model.getTargetMethod());
						// 并发设置
						methodInvJobDetailFB.setConcurrent(
								Constants.CONCURRENT_ENABLE.equals(model.getConcurrent()) ? true : false);
						methodInvJobDetailFB.afterPropertiesSet();
						JobDetail jobDetail = methodInvJobDetailFB.getObject();// 动态

						// 表达式调度构建器
						CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
								.cronSchedule(model.getCronExpression());

						// 按新的cronExpression表达式构建一个新的trigger
						trigger = TriggerBuilder.newTrigger().withIdentity(model.getJobName(), model.getJobGroup())
								.withSchedule(scheduleBuilder).build();

						// 把trigger和jobDetail注入到调度器
						scheduler.scheduleJob(jobDetail, trigger);
					} else {
						// Trigger已存在，先判断是否需要删除，如果不需要，再判定是否时间有变化
						if (Constants.TIMETASK_DISABLE.equals(status)) {
							JobKey jobKey = JobKey.jobKey(model.getJobName(), model.getJobGroup());
							scheduler.pauseTrigger(triggerKey);// 停止触发器
							scheduler.unscheduleJob(triggerKey);// 移除触发器
							scheduler.deleteJob(jobKey);// 移除任务
							continue;
						}
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
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("启动任务出错");
		}
	}

	@Override
	public void stopTimeTask(Integer id) {

		TimeTaskModel model = timeTaskMapper.getTimeTaskById(id);

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(model.getJobName(), model.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger != null) {
				JobKey jobKey = JobKey.jobKey(model.getJobName(), model.getJobGroup());
				scheduler.pauseTrigger(triggerKey);// 停止触发器
				scheduler.unscheduleJob(triggerKey);// 移除触发器
				scheduler.deleteJob(jobKey);// 移除任务
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("停止任务出错");
		}

		timeTaskMapper.stopTimeTask(id);

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

	private void startTimeTask(TimeTaskModel model) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(model.getJobName(), model.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				MethodInvokingJobDetailFactoryBean methodInvJobDetailFB = new MethodInvokingJobDetailFactoryBean();
				methodInvJobDetailFB = new MethodInvokingJobDetailFactoryBean();
				methodInvJobDetailFB.setName(model.getJobName());
				methodInvJobDetailFB.setGroup(model.getJobGroup());
				methodInvJobDetailFB.setTargetObject(SpringContextUtil.getBean(model.getTargetObject()));
				// 设置任务方法
				methodInvJobDetailFB.setTargetMethod(model.getTargetMethod());
				// 并发设置
				methodInvJobDetailFB
						.setConcurrent(Constants.CONCURRENT_ENABLE.equals(model.getConcurrent()) ? true : false);
				methodInvJobDetailFB.afterPropertiesSet();
				JobDetail jobDetail = methodInvJobDetailFB.getObject();// 动态

				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(model.getCronExpression());

				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(model.getJobName(), model.getJobGroup())
						.withSchedule(scheduleBuilder).build();

				// 把trigger和jobDetail注入到调度器
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("开始任务出错");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createActive(Integer centerId, Integer activeId, String startTime, String endTime) {
		String name = centerId + "start" + activeId;
		JobDetail jobDetail;
		jobDetail = JobBuilder.newJob((Class<? extends Job>) SpringContextUtil.getBean("startActiveTimeTaskJob").getClass())
				.withIdentity(name, name).build();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("centerId", centerId);
		jobDataMap.put("activeId", activeId);
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
}
