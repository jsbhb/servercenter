package com.zm.supplier.util;

import java.util.HashMap;
import java.util.Map;

public class StatusContrast {

	@SuppressWarnings("serial")
	private static final Map<String, Integer> hgStatus = new HashMap<String, Integer>() {
		{
			put("1-00", 3);
			put("1-01", 99);
			put("1-02", 4);
			put("1-03", 4);
			put("1-11", 4);
			put("1-12", 4);
			put("1-13", 99);
			put("1-14", 4);
			put("1-21", 4);
			put("1-22", 5);
			put("1-23", 99);
			put("1-24", 6);
			put("1-25", 99);
			put("1-99", 99);
			put("1-订单新建", 4);
			put("1-订单审核", 5);
			put("1-订单待分配", 5);
			put("1-分配完成", 5);
			put("1-装箱完成", 5);
			put("1-订单完成", 6);
			put("1-发货完成", 6);
			put("1-库存不足", 99);
			put("1-国检审核未过", 99);
			put("1-海关单证审核未过", 99);
			put("1-海关查验未过", 99);
			put("1-关贸审核失败", 99);
			put("1-平台审核失败", 99);
			put("1-运单审核失败", 99);
			put("1-支付单审核失败", 99);
			put("1-订单关闭", 99);
			put("1-身份认证失败", 99);
			put("1-申报支付单失败", 99);
			put("3-1", 3);
			put("3-2", 3);
			put("3-3", 4);
			put("3-4", 5);
			put("3-5", 5);
			put("3-6", 6);
			put("3-7", 6);
			put("3-8", 99);
			put("3-9", 99);
			put("3-10", 99);
			put("4--1", 99);
			put("4-00", 3);
			put("4-01", 99);
			put("4-02", 4);
			put("4-03", 4);
			put("4-11", 4);
			put("4-12", 4);
			put("4-13", 99);
			put("4-14", 4);
			put("4-21", 4);
			put("4-22", 5);
			put("4-23", 99);
			put("4-24", 6);
			put("4-25", 99);
			put("4-99", 99);
			put("2-00", 3);
			put("2-01", 99);
			put("2-02", 4);
			put("2-03", 4);
			put("2-11", 4);
			put("2-12", 4);
			put("2-13", 99);
			put("2-14", 4);
			put("2-21", 4);
			put("2-22", 5);
			put("2-23", 99);
			put("2-24", 6);
			put("2-25", 99);
			put("2-99", 99);
			put("6-清关开始", 4);
			put("6-清关结束", 5);
			put("6-已出库", 6);
			put("6-清关异常", 99);
			put("38-0",3);
			put("38-1",3);
			put("38-2",3);
			put("38-3",6);
			put("38--1",99);
			put("38-100",5);
			put("38-101",99);
			put("50-0",4);
			put("50-20",4);
			put("50-30",6);
			put("50-100",99);
			put("65-已发货",6);
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
