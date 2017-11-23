package com.zm.supplier.util;

import java.util.HashMap;
import java.util.Map;

public class StatusTOChiness {
	@SuppressWarnings("serial")
	private static final Map<String, String> status = new HashMap<String, String>() {
		{
			put("31", "待付款");
			put("32", "待确认");
			put("33", "通关中");
			put("34", "待推送");
			put("35", "待发货");
			put("36", "待收货");
			put("37", "已完成");
			put("38", "后台管理员取消该笔商品订单(异常订单)");
			put("39", "发货失败");
			put("310", "过期或用户取消订单");
		}
	};

	/**
	 * 通过返回码获取返回信息
	 * 
	 * @param errCode
	 *            错误码
	 * @return {String}
	 */
	public static String get(String code) {
		String result = status.get(code);
		return result != null ? result : "";
	}
}
