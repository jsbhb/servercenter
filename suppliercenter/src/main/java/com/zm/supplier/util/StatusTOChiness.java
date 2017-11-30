package com.zm.supplier.util;

import java.util.HashMap;
import java.util.Map;

public class StatusTOChiness {
	@SuppressWarnings("serial")
	private static final Map<String, String> status = new HashMap<String, String>() {
		{
			put("3-1", "待付款");
			put("3-2", "待确认");
			put("3-3", "通关中");
			put("3-4", "待推送");
			put("3-5", "待发货");
			put("3-6", "待收货");
			put("3-7", "已完成");
			put("3-8", "后台管理员取消该笔商品订单(异常订单)");
			put("3-9", "发货失败");
			put("3-10", "过期或用户取消订单");
			put("4--1", "订单删除");
			put("4-00", "未申报");
			put("4-01", "库存不足");
			put("4-11", "已报国检");
			put("4-12", "国检放行");
			put("4-13", "国检审核未过");
			put("4-14", "国检抽检");
			put("4-21", "已报海关");
			put("4-22", "单证放行");
			put("4-23", "单证审核未过");
			put("4-24", "货物放行");
			put("4-25", "海关查验未过");
			put("4-99", "已关闭");
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
