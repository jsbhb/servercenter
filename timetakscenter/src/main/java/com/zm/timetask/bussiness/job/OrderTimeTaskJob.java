package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;

@Component
public class OrderTimeTaskJob {

	@Resource
	OrderFeignClient orderFeignClient;
	
	public void closeTimeOutOrder(){
		orderFeignClient.timeTaskcloseOrder(Constants.FIRST_VERSION);
	}
	
}
