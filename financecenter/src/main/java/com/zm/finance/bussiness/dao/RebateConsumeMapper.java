package com.zm.finance.bussiness.dao;

import java.util.List;

import com.zm.finance.pojo.rebate.Rebate4Order;
import com.zm.finance.pojo.rebate.RebateConsume;

public interface RebateConsumeMapper {

	List<RebateConsume> listRebateConsume();
	
	void insertRebateConsume(Rebate4Order rebate4Order);
}
