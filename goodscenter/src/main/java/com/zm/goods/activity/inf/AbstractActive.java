package com.zm.goods.activity.inf;

import com.zm.goods.exception.ActiviteyException;

public abstract class AbstractActive<R, T> {

	protected boolean start;// 是否开始
	protected ActiveRule<T> activeRule;// 活动规则
	
	protected AbstractActive(boolean start,ActiveRule<T> activeRule){
		this.start = start;
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
		if(!start){
			throw new ActiviteyException("该活动已经结束", -1);
		}
		// 判断是否符合规则
		activeRule.processRuleJudge(t);
		return processHandle(t);
	}

	public R doFinal(T t) throws ActiviteyException {
		if(!start){
			throw new ActiviteyException("该活动已经结束", -1);
		}
		// 判断是否符合规则
		activeRule.finalRuleJudge(t);
		return finalHandle(t);
	}

	public abstract R processHandle(T t) throws ActiviteyException;

	public abstract R finalHandle(T t) throws ActiviteyException;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

}
