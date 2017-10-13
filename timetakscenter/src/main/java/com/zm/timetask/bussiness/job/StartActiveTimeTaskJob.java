package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;
import com.zm.timetask.pojo.ResultModel;
import com.zm.timetask.util.SpringContextUtil;

@Component
public class StartActiveTimeTaskJob implements Job{

	@Resource
	GoodsFeignClient goodsFeignClient;
	
	@Resource
	private Scheduler scheduler;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Integer centerId = (Integer) jobDataMap.get("centerId");
        Integer activeId = (Integer) jobDataMap.get("activeId");
        String endTime = (String) jobDataMap.get("endTime");
        ResultModel result = goodsFeignClient.startActive(Constants.FIRST_VERSION, activeId, centerId);
        if(result.isSuccess()){
        	String name = centerId + "end" + activeId;
    		JobDetail jobDetail;
    		jobDetail = JobBuilder.newJob((Class<? extends Job>) SpringContextUtil.getBean("endActiveTimeTaskJob").getClass())
    				.withIdentity(name, name).build();
    		JobDataMap endJobDataMap = jobDetail.getJobDataMap();
    		endJobDataMap.put("centerId", centerId);
    		endJobDataMap.put("activeId", activeId);

    		// 表达式调度构建器
    		String[] timeArr = endTime.split(" ");
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
	
}
