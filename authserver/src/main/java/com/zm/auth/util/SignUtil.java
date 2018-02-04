package com.zm.auth.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

public class SignUtil {

	public static String sign(Map<String, Object> param) {
		String s = sort(param);
		String str = s.substring(0, s.length() - 1);

		return DigestUtils.md5Hex(str);
	}

	public static String sort(Map<String, Object> params) {
		if (params == null) {
			throw new RuntimeException("参数不能为空");
		}
		StringBuilder sb = new StringBuilder();
		List<String> keyList = new ArrayList<String>(params.keySet());
		Collections.sort(keyList);
		for (String s : keyList) {
			if (!"sign".equalsIgnoreCase(s) && !"sign_type".equalsIgnoreCase(s)
					&& !StringUtils.isEmpty(params.get(s))) {
				sb.append(s + "=" + String.valueOf(params.get(s)) + "&");
			}
		}
		return sb.toString();
	}
}
