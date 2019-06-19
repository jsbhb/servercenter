package com.zm.order.pojo;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * @fun 商品订单产生的订单数 
 */
public class OrderGoodsCacheModel {

	private String goodsName;
	private AtomicInteger orderNum;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public AtomicInteger getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(AtomicInteger orderNum) {
		this.orderNum = orderNum;
	}
}
