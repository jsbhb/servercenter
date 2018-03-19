package com.zm.finance.bussiness.service;

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

}
