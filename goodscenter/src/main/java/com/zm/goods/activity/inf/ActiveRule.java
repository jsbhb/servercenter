package com.zm.goods.activity.inf;

import com.zm.goods.exception.ActiviteyException;

public interface ActiveRule<T> {

	/**
	 * @fun 规则初始化
	 */
	public void ruleInit();
	
	/**
	 * @fun 过程中的规则判断 出错或违法规则抛异常
	 * @param t
	 * @return
	 */
	public void processRuleJudge(T t) throws ActiviteyException;
	
	/**
	 * @fun 最终结算时的规则判断 出错或违法规则抛异常
	 * @param t
	 * @return
	 */
	public void finalRuleJudge(T t) throws ActiviteyException;
}
