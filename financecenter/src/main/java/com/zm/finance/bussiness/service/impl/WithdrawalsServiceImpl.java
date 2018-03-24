package com.zm.finance.bussiness.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zm.finance.bussiness.dao.WithdrawalsMapper;
import com.zm.finance.bussiness.service.WithdrawalsService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.AuditModel;
import com.zm.finance.pojo.ResultModel;
import com.zm.finance.pojo.withdrawals.Withdrawals;
import com.zm.finance.util.CalculationUtils;

@Service
@Transactional
public class WithdrawalsServiceImpl implements WithdrawalsService {

	@Resource
	WithdrawalsMapper withdrawalsMapper;

	@Resource
	RedisTemplate<String, Object> template;

	private static final Integer CENTER = 0;
	private static final Integer SHOP = 1;
	private static final Integer PUSHUSER = 2;

	@Override
	public ResultModel withdrawalsApply(Withdrawals withdrawals) {
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		if (CENTER.equals(withdrawals.getOperatorType())) {
			return calWithdrawals(withdrawals, hashOperations,
					Constants.CENTER_ORDER_REBATE + withdrawals.getOperatorId());
		}
		if (SHOP.equals(withdrawals.getOperatorType())) {
			return calWithdrawals(withdrawals, hashOperations,
					Constants.SHOP_ORDER_REBATE + withdrawals.getOperatorId());
		}
		if (PUSHUSER.equals(withdrawals.getOperatorType())) {
			return calWithdrawals(withdrawals, hashOperations,
					Constants.PUSHUSER_ORDER_REBATE + withdrawals.getOperatorId());
		}
		return new ResultModel(false);

	}

	private ResultModel calWithdrawals(Withdrawals withdrawals, HashOperations<String, String, String> hashOperations,
			String perfix) {
		double balance = 0;
		String canBePresentedStr = hashOperations.get(perfix, "canBePresented");
		canBePresentedStr = canBePresentedStr == null ? "0" : canBePresentedStr;
		balance = CalculationUtils.sub(Double.valueOf(canBePresentedStr), withdrawals.getOutMoney());
		if (balance < 0) {
			return new ResultModel(false, "提现金额超出可提现金额");
		} else {
			hashOperations.increment(perfix, "canBePresented", CalculationUtils.sub(0, withdrawals.getOutMoney()));
			hashOperations.increment(perfix, "alreadyPresented", withdrawals.getOutMoney());
			withdrawals.setStartMoney(Double.valueOf(canBePresentedStr));
			withdrawalsMapper.insertWithdrawals(withdrawals);
			return new ResultModel(true, "提交成功");
		}
	}

	private ResultModel backWithdrawals(Withdrawals withdrawals, HashOperations<String, String, String> hashOperations,
			String perfix) {
		hashOperations.increment(perfix, "alreadyPresented", CalculationUtils.sub(0, withdrawals.getOutMoney()));
		hashOperations.increment(perfix, "canBePresented", withdrawals.getOutMoney());
		return new ResultModel(true);
	}

	@Override
	public ResultModel withdrawalAudit(AuditModel audit) {
		Withdrawals withdrawals = withdrawalsMapper.getWithdrawals(audit.getId());
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		if (withdrawals != null) {
			if (audit.isPass()) {
				if (audit.getPayNo() == null) {
					return new ResultModel(false, "没有流水号");
				}
				withdrawalsMapper.updatePassWithdrawals(audit);
				return new ResultModel(true);
			} else {// 审核不通过要返回金额
				withdrawalsMapper.updateUnPassWithdrawals(audit);
				if (CENTER.equals(withdrawals.getOperatorType())) {
					return backWithdrawals(withdrawals, hashOperations,
							Constants.CENTER_ORDER_REBATE + withdrawals.getOperatorId());
				}
				if (SHOP.equals(withdrawals.getOperatorType())) {
					return backWithdrawals(withdrawals, hashOperations,
							Constants.SHOP_ORDER_REBATE + withdrawals.getOperatorId());
				}
				if (PUSHUSER.equals(withdrawals.getOperatorType())) {
					return backWithdrawals(withdrawals, hashOperations,
							Constants.PUSHUSER_ORDER_REBATE + withdrawals.getOperatorId());
				}
			}
		}
		return new ResultModel(false);
	}

	@Override
	public ResultModel getWithdrawal(Integer id, Integer type) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("type", type);
		return new ResultModel(true, withdrawalsMapper.listWithdrawalDetail(param));
	}

	@Override
	public Page<Withdrawals> queryForPage(Withdrawals entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getNumPerPage(), true);
		return withdrawalsMapper.selectDetailByEntity(entity);
	}

	@Override
	public Withdrawals queryWithdrawalsDetailById(Integer id) {
		return withdrawalsMapper.selectWithdrawalDetailByEntity(id);
	}

}
