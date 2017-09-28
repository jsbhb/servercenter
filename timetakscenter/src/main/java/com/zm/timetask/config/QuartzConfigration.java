package com.zm.timetask.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * ClassName: QuartzConfigration <br/>
 * Function: quartz 配置文件. <br/>
 * date: Sep 26, 2017 2:43:39 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@Configuration
public class QuartzConfigration {

	@Resource
	MyJobFactory myJobFactory;

	// 获取工厂bean
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		try {
			schedulerFactoryBean.setQuartzProperties(quartzProperties());
			schedulerFactoryBean.setJobFactory(myJobFactory);
			schedulerFactoryBean.setStartupDelay(10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return schedulerFactoryBean;
	}

	// 指定quartz.properties
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	// 创建schedule
	@Bean(name = "scheduler")
	public Scheduler scheduler() {
		return schedulerFactoryBean().getScheduler();
	}
}
