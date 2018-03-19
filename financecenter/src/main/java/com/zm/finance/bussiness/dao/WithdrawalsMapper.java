package com.zm.finance.bussiness.dao;

import java.util.Map;

import com.zm.finance.pojo.withdrawals.Withdrawals;

public interface WithdrawalsMapper {

	void insertWithdrawals(Withdrawals withdrawals);
	
	void updatePassWithdrawals(Map<String,Object> param);
	
	void updateUnPassWithdrawals(Map<String,Object> param);

}
