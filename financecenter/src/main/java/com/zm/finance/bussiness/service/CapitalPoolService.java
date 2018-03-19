package com.zm.finance.bussiness.service;

import com.zm.finance.pojo.ResultModel;

public interface CapitalPoolService {

	/**
	 * @fun 定时将redis里的资金池和资金池明细记录到数据库中
	 */
	void updateCapitalPoolTask();

	/**
	 * @fun 现金充值
	 * @param payNo
	 * @param money
	 * @param centerId
	 * @return
	 */
	ResultModel CapitalPoolRecharge(String payNo, double money, Integer centerId);

	/**
	 * @fun 返佣的钱充值资金池
	 * @param money
	 * @param centerId
	 * @return
	 */
	ResultModel CapitalPoolRecharge(double money, Integer centerId);

	
	ResultModel listcalCapitalPool();
}
