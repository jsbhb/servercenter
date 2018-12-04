package com.zm.goods.activity.inf;

import com.zm.goods.exception.ActiviteyException;

public abstract class AbstractActive<R, T> {

	protected ActiveRule<T> activeRule;// 活动规则
	
	protected AbstractActive(ActiveRule<T> activeRule){
		this.activeRule = activeRule;
		activeRule.ruleInit();//初始化规则
	}

	/**
	 * @fun
	 * @param t
	 * @return
	 * @throws ActiviteyException
	 */
	public R doProcess(T t) throws ActiviteyException {
		// 判断是否符合规则
		activeRule.processRuleJudge(t);
		return processHandle(t);
	}

	public R doFinal(T t) throws ActiviteyException {
		// 判断是否符合规则
		activeRule.finalRuleJudge(t);
		return finalHandle(t);
	}

	public abstract R processHandle(T t) throws ActiviteyException;

	public abstract R finalHandle(T t) throws ActiviteyException;

}
