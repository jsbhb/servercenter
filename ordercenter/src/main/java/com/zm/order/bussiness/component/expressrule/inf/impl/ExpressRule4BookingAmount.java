package com.zm.order.bussiness.component.expressrule.inf.impl;

import java.util.List;

import com.zm.order.bussiness.component.expressrule.inf.AbstractExpressRule;
import com.zm.order.exception.ParameterException;
import com.zm.order.exception.RuleCheckException;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.utils.CalculationUtils;

/**
 * @fun 起订额规则判断类
 * @author user
 *
 */
public class ExpressRule4BookingAmount extends AbstractExpressRule {

	private BookingAmount bookingAmount;

	public ExpressRule4BookingAmount(String json) throws ParameterException {
		bookingAmount = new BookingAmount();
		// 初始化
		try {
			render(json, bookingAmount);
		} catch (NoSuchMethodException e) {
			throw new ParameterException("参数传入有误，请联系技术处理");
		}
	}

	@Override
	public void checkParameter() throws ParameterException {
		try {
			Double minAmount = bookingAmount.getMin() == null ? 0 : Double.valueOf(bookingAmount.getMin());
			Double maxAmount = bookingAmount.getMax() == null ? -1 : Double.valueOf(bookingAmount.getMax());
			if (maxAmount != -1 && minAmount > maxAmount) {
				throw new ParameterException("起订额最大值必须大于最小值");
			}
		} catch (NumberFormatException e) {
			throw new ParameterException("金额最大最小值请传入数字");
		}

	}

	@Override
	public void checkOrderInfoRule(OrderInfo info) throws RuleCheckException {
		List<OrderGoods> goodsList = info.getOrderGoodsList();
		double total = 0;
		for (OrderGoods goods : goodsList) {
			double price = CalculationUtils.mul(goods.getActualPrice(), goods.getItemQuantity());
			total += CalculationUtils.add(total, price);
		}
		Double minAmount = bookingAmount.getMin() == null ? 0 : Double.valueOf(bookingAmount.getMin());
		Double maxAmount = bookingAmount.getMax() == null ? -1 : Double.valueOf(bookingAmount.getMax());
		if (minAmount > total || (maxAmount != -1 && total > maxAmount)) {
			String errorMsg = "该仓库类型订单金额不能小于" + minAmount;
			if (maxAmount != -1) {
				errorMsg += ",不能大于" + maxAmount;
			}
			throw new RuleCheckException(errorMsg);
		}
	}

	public static void main(String[] args) throws ParameterException {
		ExpressRule4BookingAmount a = new ExpressRule4BookingAmount("{\"max\":\"100.1\",\"min\":\"100\"}");
		a.checkParameter();
	}

	/**
	 * @fun 起订额判断内部类，参数设定
	 * @author user
	 *
	 */
	private class BookingAmount {
		private String min;
		private String max;

		public String getMin() {
			return min;
		}

		@SuppressWarnings("unused")
		public void setMin(String min) {
			this.min = min;
		}

		public String getMax() {
			return max;
		}

		@SuppressWarnings("unused")
		public void setMax(String max) {
			this.max = max;
		}

	}

}
