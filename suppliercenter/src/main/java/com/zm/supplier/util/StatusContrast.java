package com.zm.supplier.util;

import java.util.HashMap;
import java.util.Map;

public class StatusContrast {

	@SuppressWarnings("serial")
	private static final Map<String, Integer> hgStatus = new HashMap<String, Integer>() {
		{
			put("订单新建", 4);
			put("订单审核", 5);
			put("订单待分配", 5);
			put("分配完成", 5);
			put("装箱完成", 5);
			put("订单完成", 6);
			put("库存不足", 99);
			put("国检审核未过", 99);
			put("海关单证审核未过", 99);
			put("海关查验未过", 99);
			put("关贸审核失败", 99);
			put("平台审核失败", 99);
			put("运单审核失败", 99);
			put("支付单审核失败", 99);
			put("订单关闭", 99);
			put("身份认证失败", 99);
			put("申报支付单失败", 99);
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
