package com.zm.supplier.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

public class SignUtil {

	public static String TianTianSign(String msg, String appSecret, String date) {
		String toSignStr = msg + appSecret + date;
		String sign = new String(Base64.encodeBase64(DigestUtils.md5(toSignStr))).toUpperCase();
		try {
			return URLEncoder.encode(sign, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String liangYouSign(Map<String, Object> param) {
		String s = sort(param);
		String str = s.substring(0, s.length() - 1);

		return DigestUtils.md5Hex(str);
	}

	@SuppressWarnings("unchecked")
	public static String XinYunSing(String msg, String appSecret) {
		Map<String, Object> params = JSONUtil.parse(msg, Map.class);
		if (params.get("items") != null) {
			String items = params.get("items").toString();
			items = items.replace("=", ":").replace(" ", "");
			params.put("items", items);
		}
		String s = sort(params);
		String str = s.substring(0, s.length() - 1) + appSecret;
		return DigestUtils.md5Hex(getContentBytes(str, "UTF-8"));
	}

	/**
	 * @fun 获取byte数组
	 * @param content
	 * @param charset
	 * @return
	 */
	public static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}

	/**
	 * @fun 排序
	 * @param params
	 * @return
	 */
	public static String sort(Map<String, Object> params) {
		if (params == null) {
			return null;
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

	public static String fuBangSign(String msg, String appSecret) {
		String toSignStr = appSecret + msg;
		return DigestUtils.md5Hex(toSignStr);
	}

}
