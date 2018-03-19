package com.zm.finance.bussiness.dao;

import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.withdrawals.Withdrawals;

public interface WithdrawalsMapper {

	void insertWithdrawals(Withdrawals withdrawals);
	
	void updatePassWithdrawals(AuditModel audit);
	
	void updateUnPassWithdrawals(AuditModel audit);
	
	Withdrawals getWithdrawals(Integer id);

}
