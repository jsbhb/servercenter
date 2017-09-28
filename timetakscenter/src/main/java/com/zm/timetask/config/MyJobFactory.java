package com.zm.timetask.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: MyJobFactory <br/>
 * Function: 自定义MyJobFactory，解决spring不能在quartz中注入bean的问题. <br/>
 * date: Sep 26, 2017 2:42:15 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@Component
public class MyJobFactory extends AdaptableJobFactory {

	@Autowired  
    private AutowireCapableBeanFactory capableBeanFactory;  
     
    @Override  
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {  
      Object jobInstance = super.createJobInstance(bundle);  
      capableBeanFactory.autowireBean(jobInstance); //这一步解决不能spring注入bean的问题  
      return jobInstance;  
      
    }  
}
