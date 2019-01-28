package com.zm.thirdcenter.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
/**
 * 
 * @author user
 *
 */
public class ConfUtils {

	private static ResourceBundle res = ResourceBundle.getBundle("conf");
	private static Map<String, String> confMap = null;

	/**
	 * 获取urlMap
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getConfMap() {
		if (confMap != null && !confMap.isEmpty()) {
			return confMap;
		}
		confMap = new HashMap<String, String>();
		Enumeration e = res.getKeys();
		while (e.hasMoreElements()) {
			String key = e.nextElement().toString();
			String value = get(key);
			confMap.put(key, value);
		}
		return confMap;
	}


	private static String get(String key) {
		return res.getString(key);
	}

}
