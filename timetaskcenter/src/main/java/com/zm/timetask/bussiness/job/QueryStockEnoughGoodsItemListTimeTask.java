package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.GoodsFeignClient;

@Component
public class QueryStockEnoughGoodsItemListTimeTask implements Job{

	@Resource
	GoodsFeignClient goodsFeignClient;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		goodsFeignClient.stockQtyEnoughList(Constants.FIRST_VERSION);
	}

}
