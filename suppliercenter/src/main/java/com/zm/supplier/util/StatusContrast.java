package com.zm.supplier.util;

import java.util.HashMap;
import java.util.Map;

public class StatusContrast {

	@SuppressWarnings("serial")
	private static final Map<String, Integer> hgStatus = new HashMap<String, Integer>() {
		{
			put("1订单新建", 4);
			put("1订单审核", 5);
			put("1订单待分配", 5);
			put("1分配完成", 5);
			put("1装箱完成", 5);
			put("1订单完成", 6);
			put("1库存不足", 99);
			put("1国检审核未过", 99);
			put("1海关单证审核未过", 99);
			put("1海关查验未过", 99);
			put("1关贸审核失败", 99);
			put("1平台审核失败", 99);
			put("1运单审核失败", 99);
			put("1支付单审核失败", 99);
			put("1订单关闭", 99);
			put("1身份认证失败", 99);
			put("1申报支付单失败", 99);
			put("31", 3);
			put("32", 3);
			put("33", 4);
			put("34", 5);
			put("35", 5);
			put("36", 6);
			put("37", 6);
			put("38", 99);
			put("39", 99);
			put("310", 99);
		}
	};

	/**
	 * 通过返回码获取返回信息
	 * 
	 * @param errCode
	 *            错误码
	 * @return {String}
	 */
	public static Integer get(String code) {
		Integer result = hgStatus.get(code);
		return result != null ? result : 99;
	}
}
