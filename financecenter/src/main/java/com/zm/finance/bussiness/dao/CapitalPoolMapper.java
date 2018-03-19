package com.zm.finance.bussiness.dao;

import java.util.List;

import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.pojo.capitalpool.CapitalPoolDetail;
import com.zm.finance.pojo.refilling.Refilling;

public interface CapitalPoolMapper {

	List<CapitalPool> listCenterCapitalPool();
	
	void updateCapitalPool(List<CapitalPool> list);
	
	void insertCapitalPoolDetail(List<CapitalPoolDetail> list);

	Refilling getRefilling(Integer id);

	void updatePassRechargeApply(Integer id);

	void updateUnPassRechargeApply(Integer id);
	
	void insertRefillingDetail(Refilling refilling);
}
