package com.zm.order.pojo.bo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @fun 当天订单统计model
 * @author user
 *
 */
public class IntradayOrderBO {

	private AtomicInteger produce;// 当天产生的订单数
	private AtomicInteger deliver;// 当天发货的订单数
	private AtomicInteger cancel;// 当天取消的订单数

	public IntradayOrderBO() {
		produce = new AtomicInteger(0);
		deliver = new AtomicInteger(0);
		cancel = new AtomicInteger(0);
	}
	
	public void add(IntradayOrderBO intradayOrderBO){
		produce.addAndGet(intradayOrderBO.getProduce().get());
		deliver.addAndGet(intradayOrderBO.getDeliver().get());
		cancel.addAndGet(intradayOrderBO.getCancel().get());
	}

	public int incrProduce() {
		return produce.incrementAndGet();
	}

	public int incrDeliver() {
		return deliver.incrementAndGet();
	}

	public int incrCancel() {
		return cancel.incrementAndGet();
	}

	public AtomicInteger getProduce() {
		return produce;
	}

	public void setProduce(AtomicInteger produce) {
		this.produce = produce;
	}
	
	public void setProduce(String produce) {
		this.produce = new AtomicInteger(Integer.valueOf(produce));
	}

	public AtomicInteger getDeliver() {
		return deliver;
	}

	public void setDeliver(AtomicInteger deliver) {
		this.deliver = deliver;
	}
	
	public void setDeliver(String deliver) {
		this.deliver = new AtomicInteger(Integer.valueOf(deliver));
	}

	public AtomicInteger getCancel() {
		return cancel;
	}

	public void setCancel(AtomicInteger cancel) {
		this.cancel = cancel;
	}
	
	public void setCancel(String cancel) {
		this.cancel = new AtomicInteger(Integer.valueOf(cancel));
	}
}
