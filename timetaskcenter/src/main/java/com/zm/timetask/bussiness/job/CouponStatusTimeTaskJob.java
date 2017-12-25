package com.zm.timetask.bussiness.job;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.ActivityFeignClient;
import com.zm.timetask.feignclient.UserFeignClient;
import com.zm.timetask.pojo.ResultModel;
/**
 * @fun 改变优惠券状态
 * @author wqy
 *
 */
@Component
public class CouponStatusTimeTaskJob implements Job{

	@Resource
	UserFeignClient userFeignClient;
	
	@Resource
	ActivityFeignClient activityFeignClient;
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		ResultModel resultModel = userFeignClient.getCenterId(Constants.FIRST_VERSION);
		if(resultModel.isSuccess()){
			List<Integer> centerIdList = (List<Integer>) resultModel.getObj();
			if(centerIdList != null){
				activityFeignClient.updateCouponStatus(Constants.FIRST_VERSION, centerIdList);
			}
		}
	}
}
