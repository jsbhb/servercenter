package com.zm.supplier.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import com.zm.supplier.pojo.callback.base.CallBackBase;

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
	
	@SuppressWarnings("unchecked")
	public static String callBackSign(CallBackBase base, String appSecret){
		Map<String,Object> param = JSONUtil.parse(JSONUtil.toJson(base), Map.class);
		param.put("appSecret", appSecret);
		String s = sort(param);
		String str = s.substring(0, s.length() - 1);

		return DigestUtils.md5Hex(str);
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
	public static String sort(Map<String, ? extends Object> params) {
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

	public static String qianFengSign(String appSecret, Map<String, String> param) {
		if (param == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		List<String> keyList = new ArrayList<String>(param.keySet());
		Collections.sort(keyList);
		for (String s : keyList) {
			sb.append(s + String.valueOf(param.get(s)));
		}
		String str = sb.toString() + appSecret;

		return DigestUtils.md5Hex(str);
	}

	public static String qianFengSign(String appKey, String appSecret) {
		
		return DigestUtils.md5Hex(appKey+appSecret);
	}
	
	/**
	 * 签名
	 * 
	 * @param params
	 *            参数
	 * @return 签名结果
	 */
	public static String edbSignature(Map<String, String> params,String appScret,String token,String appKey) {
		Map<String, String> treeMap = new TreeMap<String, String>(comparator);
		treeMap.putAll(params);
		treeMap.put("appscret", appScret);
		treeMap.put("token", token);
		// 拼接要签名的字符串
		StringBuilder builder = new StringBuilder(appKey);
		for (String key : treeMap.keySet()) {
			if ("".equals(key) || "".equals(treeMap.get(key)))
				continue;
			builder.append(key).append(treeMap.get(key));
		}
		System.out.println("签名明文:" + builder);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(builder.toString().getBytes("utf-8"));
			builder.setLength(0);
			for (byte b : bytes) {
				String hx = Integer.toHexString(b & 0XFF);
				builder.append(hx.length() == 1 ? "0" + hx : hx);
			}
			return builder.toString().toUpperCase();
		} catch (Exception e) {
			return "签名异常";
		}
	}

	/**
	 * 比较器
	 */
	private static Comparator<String> comparator = new Comparator<String>() {
		public int compare(String k1, String k2) {
			return k1.compareToIgnoreCase(k2);
		}
	};

}
