package com.zm.finance.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.withdrawals.Withdrawals;

public interface WithdrawalsService {

	/**
	 * @fun 提现申请
	 * @param withdrawals
	 * @return
	 */
	ResultModel withdrawalsApply(Withdrawals withdrawals);

	/**
	 * @fun 提现审核
	 * @param audit
	 * @return
	 */
	ResultModel withdrawalAudit(AuditModel audit);

	/**
	 * @fun 获取提现记录
	 * @param id
	 * @param type
	 * @return
	 */
	ResultModel getWithdrawal(Integer id, Integer type);

	/**
	 * @fun 获取提现记录
	 * @param id
	 * @param type
	 * @return
	 */
	Page<Withdrawals> queryForPage(Withdrawals entity);

}
