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
			put("zhongtong", "中通速递");
			put("yunda", "韵达快递");
			put("shentong", "申通快递");
			put("ems", "EMS");
			put("yuantong", "圆通速递");
			put("tiantian", "天天快递");
			put("huitongkuaidi", "百世快递");
			put("zhaijisong", "宅急送");
			put("youzhengguonei", "邮政平邮/小包");
			put("debangwuliu", "德邦");
			put("annengwuliu", "安能物流");
			put("顺丰速运", "顺丰快递");
			put("邮政速递", "EMS");
			put("中通速递", "中通速递");
			put("北仑军通", "圆通速递");
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
