package com.zm.timetask.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.timetask.pojo.TimeTaskModel;

/**
 * ClassName: TimeTaskService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Sep 26, 2017 3:30:04 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public interface TimeTaskService {

	/**
	 * runAllTimeTask:启动所有启用的定时任务. <br/>
	 * 
	 * @author wqy
	 * @since JDK 1.7
	 */
	void runAllTimeTask();

	/**
	 * stopTimeTask:停止某个任务. <br/>
	 * 
	 * @author wqy
	 * @param id
	 * @since JDK 1.7
	 */
	void stopTimeTask(Integer id);

	/**
	 * startTimeTask:开始某个任务. <br/>
	 * 
	 * @author wqy
	 * @param id
	 * @since JDK 1.7
	 */
	void startTimeTask(Integer id);

	/**
	 * updateTimeTask:修改任务调度时间. <br/>
	 * 
	 * @author wqy
	 * @param TimeTaskModel
	 * @since JDK 1.7
	 */
	void updateTimeTask(TimeTaskModel model);
	
	/**
	 * saveTimeTask:新增任务调度. <br/>
	 * 
	 * @author wqy
	 * @param TimeTaskModel
	 * @since JDK 1.7
	 */
	void saveTimeTask(TimeTaskModel model);
	
	/**
	 * @fun dynamicSchedule:动态生成定时器. type 0:活动定时器；1：优惠券定时器<br/>
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 * @author wqy
	 * @param 
	 * @since JDK 1.7
	 */
	void dynamicSchedule(Integer centerId, String activeId, String startTime, String endTime, Integer type);
	
	/**
	 * queryAllTimeTask:查询所有任务调度. <br/>
	 * 
	 * @author why
	 * @param TimeTaskModel
	 * @since JDK 1.7
	 */
	Page<TimeTaskModel> queryAllTimeTask(TimeTaskModel entity);
	
	/**
	 * queryAllTimeTash:根据编号查询任务调度. <br/>
	 * 
	 * @author wqy
	 * @param TimeTaskModel
	 * @since JDK 1.7
	 */
	TimeTaskModel queryTimeTaskById(Integer id);
	
}
