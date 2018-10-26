package com.zm.timetask.bussiness.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.FinanceFeignClient;

@Component
public class RebateCheckTimeTaskJob {

	@Resource
	FinanceFeignClient financeFeignClient;

	public void removeActiveTimeTask() throws Exception {
		financeFeignClient.rebateCheck(Constants.FIRST_VERSION);
	}
}
