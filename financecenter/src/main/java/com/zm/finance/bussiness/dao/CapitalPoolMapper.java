package com.zm.finance.bussiness.dao;

import java.util.List;

import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.pojo.capitalpool.CapitalPoolDetail;

public interface CapitalPoolMapper {

	List<CapitalPool> listCenterCapitalPool();
	
	void updateCapitalPool(List<CapitalPool> list);
	
	void insertCapitalPoolDetail(List<CapitalPoolDetail> list);
}
