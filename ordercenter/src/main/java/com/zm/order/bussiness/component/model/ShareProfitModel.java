package com.zm.order.bussiness.component.model;

import java.io.Serializable;

public class ShareProfitModel implements Serializable{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;

	private Double registerProfit;//注册地分润
	
	private Double consumeProfit;//消费地分润
	
	private Double orderProfit;//订单利润
	
	private String orderId;
	
	private Double orderAmount;//订单金额
	
	private boolean crossArea; //是否跨区域

	public Double getRegisterProfit() {
		return registerProfit;
	}

	public void setRegisterProfit(Double registerProfit) {
		this.registerProfit = registerProfit;
	}

	public Double getConsumeProfit() {
		return consumeProfit;
	}

	public void setConsumeProfit(Double consumeProfit) {
		this.consumeProfit = consumeProfit;
	}

	public Double getOrderProfit() {
		return orderProfit;
	}

	public void setOrderProfit(Double orderProfit) {
		this.orderProfit = orderProfit;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public boolean isCrossArea() {
		return crossArea;
	}

	public void setCrossArea(boolean crossArea) {
		this.crossArea = crossArea;
	}
	
}
