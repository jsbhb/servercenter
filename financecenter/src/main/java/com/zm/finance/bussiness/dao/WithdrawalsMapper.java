package com.zm.finance.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.withdrawals.Withdrawals;

public interface WithdrawalsMapper {

	void insertWithdrawals(Withdrawals withdrawals);
	
	void updatePassWithdrawals(AuditModel audit);
	
	void updateUnPassWithdrawals(AuditModel audit);
	
	Withdrawals getWithdrawals(Integer id);

	List<Withdrawals> listWithdrawalDetail(Integer gradeId);

	Page<Withdrawals> selectDetailByEntity(Withdrawals withdrawals);

	Withdrawals selectWithdrawalDetailByEntity(Integer id);

}
