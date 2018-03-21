package com.zm.finance.bussiness.service;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.rebate.RebateDetail;

public interface RebateService {

	/**
	 * @fun 定时将redis里的返佣记录保存到数据库
	 */
	void updateRebateTask();

	/**
	 * @fun 获取返佣
	 * @param id
	 * @param type 0区域中心，1店铺，2推手
	 * @return
	 */
	ResultModel getRebate(Integer id, Integer type);

	Page<RebateDetail> getRebateDetail(RebateDetail entity);

}
