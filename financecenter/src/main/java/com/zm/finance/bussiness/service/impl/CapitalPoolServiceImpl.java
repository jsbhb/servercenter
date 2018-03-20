package com.zm.finance.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.finance.bussiness.dao.CapitalPoolMapper;
import com.zm.finance.bussiness.service.CapitalPoolService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.pojo.capitalpool.CapitalPoolDetail;
import com.zm.finance.pojo.refilling.Refilling;
import com.zm.finance.util.CalculationUtils;
import com.zm.finance.util.JSONUtil;

@Service
@Transactional
public class CapitalPoolServiceImpl implements CapitalPoolService {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	CapitalPoolMapper capitalPoolMapper;

	@Override
	public void updateCapitalPoolTask() {
		Set<String> keys = template.keys(Constants.CAPITAL_PERFIX + "*");
		if (keys == null) {
			return;
		}
		Map<String, String> result = new HashMap<String, String>();
		List<CapitalPool> poolList = new ArrayList<CapitalPool>();
		List<CapitalPoolDetail> detailList = new ArrayList<CapitalPoolDetail>();
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		for (String key : keys) {
			result = hashOperations.entries(key);
			if (result != null) {
				poolList.add(JSONUtil.parse(JSONUtil.toJson(result), CapitalPool.class));
			}
		}

		List<Object> poolDetailList = template.opsForList().range(Constants.CAPITAL_DETAIL, 0, -1);
		if (poolDetailList != null) {
			for (Object obj : poolDetailList) {
				detailList.add(JSONUtil.parse(obj.toString(), CapitalPoolDetail.class));
			}
		}
		capitalPoolMapper.insertCapitalPoolDetail(detailList);
		capitalPoolMapper.updateCapitalPool(poolList);
		template.opsForList().trim(Constants.CAPITAL_DETAIL, poolDetailList.size(), -1);// 删除以保存的记录
	}

	@Override
	public ResultModel CapitalPoolRecharge(String payNo, double money, Integer centerId) {
		capitalPoolCharge(payNo, money, centerId, Constants.CASH);
		return new ResultModel(true);
	}

	private void capitalPoolCharge(String payNo, double money, Integer centerId, Integer bussinessType) {
		template.opsForHash().increment(Constants.CAPITAL_PERFIX + centerId, "money", money);// 增加余额
		template.opsForHash().increment(Constants.CAPITAL_PERFIX + centerId, "countMoney", money);// 增加累计金额
		CapitalPoolDetail detail = new CapitalPoolDetail();
		detail.setBussinessType(bussinessType);
		detail.setCenterId(centerId);
		detail.setMoney(money);
		detail.setPayNo(payNo);
		detail.setPayType(Constants.INCOME);
		detail.setRemark("资金池充值");
		List<CapitalPoolDetail> detailList = new ArrayList<CapitalPoolDetail>();
		detailList.add(detail);
		capitalPoolMapper.insertCapitalPoolDetail(detailList);
	}

	@Override
	public ResultModel CapitalPoolRechargeAudit(AuditModel audit) {
		Refilling refilling = capitalPoolMapper.getRefilling(audit.getId());
		if (refilling != null) {
			if (audit.isPass()) {
				capitalPoolCharge(null, refilling.getMoney(), refilling.getCenterId(), Constants.REBATE);
				capitalPoolMapper.updatePassRechargeApply(audit.getId());
			} else {
				capitalPoolMapper.updateUnPassRechargeApply(audit);
				template.opsForHash().increment(Constants.CENTER_ORDER_REBATE + refilling.getCenterId(),
						"canBePresented", refilling.getMoney());
				template.opsForHash().increment(Constants.CENTER_ORDER_REBATE + refilling.getCenterId(), "refilling",
						CalculationUtils.sub(0, refilling.getMoney()));// 已反充金额增加

			}
		}
		return new ResultModel(true);
	}

	@Override
	public ResultModel listcalCapitalPool(CapitalPool capitalPool) {
		List<CapitalPool> poolList = new ArrayList<CapitalPool>();
		Map<String, String> result = null;
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		if (capitalPool.getCenterId() == null) {
			Set<String> keys = template.keys(Constants.CAPITAL_PERFIX + "*");
			if (keys != null) {
				for (String key : keys) {
					result = hashOperations.entries(key);
					if (result != null) {
						poolList.add(JSONUtil.parse(JSONUtil.toJson(result), CapitalPool.class));
					}
				}
			}
		} else {
			result = hashOperations.entries(Constants.CAPITAL_PERFIX + capitalPool.getCenterId());
			if (result != null) {
				poolList.add(JSONUtil.parse(JSONUtil.toJson(result), CapitalPool.class));
			}
		}

		return new ResultModel(true, poolList);
	}

	@Override
	public ResultModel reChargeCapitalApply(Refilling refilling) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		double balance = 0;
		String canBePresentedStr = hashOperations.get(Constants.CENTER_ORDER_REBATE + refilling.getCenterId(),
				"canBePresented");
		balance = CalculationUtils.sub(Double.valueOf(canBePresentedStr), refilling.getMoney());
		if (balance < 0) {
			return new ResultModel(false, "反充金额超出可提现金额");
		}
		capitalPoolMapper.insertRefillingDetail(refilling);
		hashOperations.increment(Constants.CENTER_ORDER_REBATE + refilling.getCenterId(), "canBePresented",
				CalculationUtils.sub(0, refilling.getMoney()));
		hashOperations.increment(Constants.CENTER_ORDER_REBATE + refilling.getCenterId(), "refilling",
				refilling.getMoney());// 已反充金额增加
		return new ResultModel(true, "提交成功");
	}

	@Override
	public ResultModel CapitalPoolRecordRegister(Integer centerId) {
		CapitalPool record = capitalPoolMapper.selectRecordByCenterId(centerId);
		if (record == null) {
			capitalPoolMapper.insertCapitalPoolRecord(centerId);
			HashOperations<String, String, String> hashOperations = template.opsForHash();
			String key = Constants.CAPITAL_PERFIX + centerId;
			if (!template.hasKey(key)){// 初始化redis里的资金池
				Map<String, String> mapForRedis = new HashMap<String, String>();
				mapForRedis.put("centerId", centerId.toString());
				mapForRedis.put("money", "0");
				mapForRedis.put("frozenMoney", "0");
				mapForRedis.put("preferential", "0");
				mapForRedis.put("frozenPreferential", "0");
				mapForRedis.put("useMoney", "0");
				mapForRedis.put("usePreferential", "0");
				mapForRedis.put("countMoney", "0");
				mapForRedis.put("countPreferential", "0");
				hashOperations.putAll(Constants.CAPITAL_PERFIX + centerId, mapForRedis);
			}
		}
		return new ResultModel(true);
	}
}
