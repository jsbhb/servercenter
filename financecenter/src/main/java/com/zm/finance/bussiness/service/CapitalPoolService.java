package com.zm.finance.bussiness.service;

import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.pojo.refilling.Refilling;

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
	ResultModel CapitalPoolRechargeAudit(AuditModel audit);

	/**
	 * @fun 获取区域中心资金池
	 * @return
	 */
	ResultModel listcalCapitalPool(CapitalPool capitalPool);

	/**
	 * @fun 返佣进行资金池充值申请
	 * @param refilling
	 * @return
	 */
	ResultModel reChargeCapitalApply(Refilling refilling);
}
