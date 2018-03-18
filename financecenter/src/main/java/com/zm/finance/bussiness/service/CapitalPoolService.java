package com.zm.finance.bussiness.service;

public interface CapitalPoolService {

	/**
	 * @fun 定时将redis里的资金池和资金池明细记录到数据库中
	 */
	void updateCapitalPoolTask();
}
