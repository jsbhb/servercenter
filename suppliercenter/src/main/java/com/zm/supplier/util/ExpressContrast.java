package com.zm.supplier.util;

import java.util.HashMap;
import java.util.Map;

public class ExpressContrast {

	@SuppressWarnings("serial")
	private static final Map<String, String> express = new HashMap<String, String>() {
		{
			put("SF", "顺丰快递");
			put("EMS", "EMS");
			put("POSTAM", "邮政平邮/小包");
			put("ZTO", "中通速递");
			put("STO", "申通快递");
			put("YTO", "圆通速递");
			put("JD", "京东快递");
			put("BEST", "百世快递");
			put("YUNDA", "韵达快递");
			put("TTKDEX", "天天快递");
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
		String result = express.get(code);
		return result != null ? result : "";
	}
}
