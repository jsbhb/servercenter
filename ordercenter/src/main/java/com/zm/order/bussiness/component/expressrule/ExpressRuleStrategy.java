package com.zm.order.bussiness.component.expressrule;

import java.util.List;

import com.zm.order.bussiness.component.expressrule.factory.ExpressRuleFactory;
import com.zm.order.bussiness.component.expressrule.inf.AbstractExpressRule;
import com.zm.order.exception.ParameterException;
import com.zm.order.exception.RuleCheckException;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.pojo.bo.ExpressRule;

/**
 * @fun 模板规则判断
 * @author user
 *
 */
public class ExpressRuleStrategy {

	private List<AbstractExpressRule> list;
	
	public ExpressRuleStrategy(List<ExpressRule> ruleList) throws ParameterException{
		list = ExpressRuleFactory.getExpressRule(ruleList);
	}
	
	public void judgeExpressRule(OrderInfo info) throws RuleCheckException{
		for(AbstractExpressRule rule : list){
			rule.checkOrderInfoRule(info);
		}
	}
	
	public void checkExpressRuleParam() throws ParameterException {
		for(AbstractExpressRule rule : list){
			rule.checkParameter();
		}
	}
}
