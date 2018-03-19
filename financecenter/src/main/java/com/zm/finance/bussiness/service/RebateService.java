package com.zm.finance.bussiness.service;

public interface RebateService {

	/**
	 * @fun 定时将redis里的返佣记录保存到数据库
	 */
	void updateRebateTask();

}
