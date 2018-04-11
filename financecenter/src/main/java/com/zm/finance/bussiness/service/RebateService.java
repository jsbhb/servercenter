package com.zm.finance.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.RebateSearchModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.rebate.Rebate;
import com.zm.finance.pojo.rebate.RebateDetail;

public interface RebateService {

	/**
	 * @fun 定时将redis里的返佣记录保存到数据库
	 */
	void updateRebateTask();

	/**
	 * @fun 获取返佣
	 * @param gradeId
	 * @return
	 */
	ResultModel getRebate(Integer gradeId);

	Page<RebateDetail> getRebateDetail(RebateDetail entity);

	Page<Rebate> listRebate(RebateSearchModel search);

}
