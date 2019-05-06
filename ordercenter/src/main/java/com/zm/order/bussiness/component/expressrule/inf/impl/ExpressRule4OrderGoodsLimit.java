package com.zm.order.bussiness.component.expressrule.inf.impl;

import java.util.List;

import com.zm.order.bussiness.component.expressrule.inf.AbstractExpressRule;
import com.zm.order.exception.ParameterException;
import com.zm.order.exception.RuleCheckException;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;

public class ExpressRule4OrderGoodsLimit extends AbstractExpressRule {

	private OrderGoodsLimit orderGoodsLimit;

	public ExpressRule4OrderGoodsLimit(String json) throws ParameterException {
		orderGoodsLimit = new OrderGoodsLimit();
		// 初始化
		try {
			render(json, orderGoodsLimit);
		} catch (NoSuchMethodException e) {
			throw new ParameterException("参数传入有误，请联系技术处理");
		}
	}

	@Override
	public void checkParameter() throws ParameterException {
		try {
			int min = orderGoodsLimit.getMin() == null ? 1 : Integer.valueOf(orderGoodsLimit.getMin());
			int max = orderGoodsLimit.getMax() == null ? 1 : Integer.valueOf(orderGoodsLimit.getMax());
			if (max <= 0 || min > max) {
				throw new ParameterException("设置有误");
			}
		} catch (NumberFormatException e) {
			throw new ParameterException("请传入整数");
		}
	}

	@Override
	public void checkOrderInfoRule(OrderInfo info) throws RuleCheckException {
		List<OrderGoods> goodsList = info.getOrderGoodsList();
		int tdq = goodsList.size();
		int min = orderGoodsLimit.getMin() == null ? 1 : Integer.valueOf(orderGoodsLimit.getMin());
		int max = orderGoodsLimit.getMax() == null ? 1 : Integer.valueOf(orderGoodsLimit.getMax());
		if (tdq > max || tdq < min) {
			String errorMsg = "该仓库订单商品种类(数量可以多个)不能大于" + max + ",且不能小于" + min;
			throw new RuleCheckException(errorMsg);
		}
	}

	public OrderGoodsLimit getOrderGoodsLimit() {
		return orderGoodsLimit;
	}

	public void setOrderGoodsLimit(OrderGoodsLimit orderGoodsLimit) {
		this.orderGoodsLimit = orderGoodsLimit;
	}

	private class OrderGoodsLimit {
		private String max;
		private String min;

		public String getMax() {
			return max;
		}

		@SuppressWarnings("unused")
		public void setMax(String max) {
			this.max = max;
		}

		public String getMin() {
			return min;
		}

		@SuppressWarnings("unused")
		public void setMin(String min) {
			this.min = min;
		}
	}
}
