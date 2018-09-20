package com.zm.order.bussiness.component.expressrule.factory;

import java.util.ArrayList;
import java.util.List;

import com.zm.order.bussiness.component.expressrule.inf.AbstractExpressRule;
import com.zm.order.bussiness.component.expressrule.inf.impl.ExpressRule4BookingAmount;
import com.zm.order.exception.ParameterException;
import com.zm.order.pojo.bo.ExpressRule;

public class ExpressRuleFactory {

	private static final int BOOKING_AMOUNT = 1;//金额判断

	/**
	 * @fun 获取指定的规则判断类集合
	 * @param ruleList
	 * @return
	 * @throws ParameterException
	 */
	public static List<AbstractExpressRule> getExpressRule(List<ExpressRule> ruleList) throws ParameterException {
		List<AbstractExpressRule> list = new ArrayList<AbstractExpressRule>();
		for (ExpressRule rule : ruleList) {
			switch (rule.getType()) {
			case BOOKING_AMOUNT:
				list.add(new ExpressRule4BookingAmount(rule.getJson()));
				break;
			default:
				throw new ParameterException("没有对应的规则，请先联系技术增加规则");
			}
		}
		return list;
	}
}
