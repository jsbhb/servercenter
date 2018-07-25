package com.zm.finance.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.capitalpool.CapitalPoolDetail;
import com.zm.finance.pojo.capitalpool.CapitalSearchParam;
import com.zm.finance.pojo.refilling.Refilling;

public interface CapitalPoolService {

	/**
	 * @fun 定时将redis里的资金池和资金池明细记录到数据库中
	 */
	void updateCapitalPoolTask();

	/**
	 * @fun 现金充值
	 * @param payNo
	 * @param money
	 * @param centerId
	 * @return
	 */
	ResultModel CapitalPoolRecharge(String payNo, double money, Integer centerId);

	/**
	 * @fun 返佣的钱充值资金池
	 * @param money
	 * @param centerId
	 * @return
	 */
	ResultModel CapitalPoolRechargeAudit(AuditModel audit);

	/**
	 * @fun 获取区域中心资金池
	 * @return
	 */
	ResultModel listcalCapitalPool(CapitalSearchParam searchParam);

	/**
	 * @fun 返佣进行资金池充值申请
	 * @param refilling
	 * @return
	 */
	ResultModel reChargeCapitalApply(Refilling refilling);
	
	/**
	 * @fun 添加资金池记录
	 * @param centerId
	 * @return
	 */
	ResultModel CapitalPoolRecordRegister(Integer centerId);

	/**
	 * @fun 获取返充记录
	 * @param id
	 * @param type
	 * @return
	 */
	Page<Refilling> queryForPage(Refilling entity);
	
	/**
	 * @fun 查询返充记录
	 * @param centerId
	 * @return
	 */
	ResultModel queryRefillingDetailById(Integer id);

	/**
	 * @fun 资金池充值多了后清算
	 * @param centerId
	 * @param money
	 * @return
	 */
	ResultModel liquidation(Integer centerId, Double money);

	ResultModel getCapitalPoolOverview(List<Integer> list);

	ResultModel listCapitalPoolDetail(CapitalPoolDetail detail);

	ResultModel addCapitalpool(CapitalPoolDetail detail);
}
