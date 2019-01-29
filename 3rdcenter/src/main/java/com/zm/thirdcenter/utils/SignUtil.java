package com.zm.thirdcenter.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

public class SignUtil {

	/**
	 * 获得分享链接的签名。
	 * 
	 * @param ticket
	 * @param nonceStr
	 * @param timeStamp
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getSignature(String ticket, String nonceStr, String timeStamp, String url) {
		String sKey = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + url;
		System.out.println(sKey);
		return getSignature(sKey);
	}

	/**
	 * 验证签名。
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static String getSignature(String sKey) {
		String ciphertext = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(sKey.toString().getBytes());
			ciphertext = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ciphertext.toLowerCase();
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

	public static String decrypt(String accessKey, String accessSecret, Map<String, Object> map) {
		List<String> sortedKeyList = keySort(map);
		StringBuilder builder = new StringBuilder();
		for (String key : sortedKeyList) {
			if (!key.equalsIgnoreCase("accesskey")) {
				String value = String.valueOf(map.get(key));
				if (key.equalsIgnoreCase("stockInVoucherSkus")) {
					value = JSONUtil.toJson(map.get(key));
				}
				if (key.equalsIgnoreCase("stockOutVoucherSkus")) {
					value = JSONUtil.toJson(map.get(key));
				}
				if (value != null && !"null".equals(value)) {
					builder.append(key);
					builder.append(value);
				}
			}
		}
		builder.append(accessSecret);
		builder.insert(0, accessKey);
		return builder.toString();
	}

	public static String sha1(String decrypt) throws IOException {
		try {
			// 指定 sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes());
			// 获取字节数组
			byte messageDigest[] = digest.digest(); // Create Hex String
			StringBuffer hexString = new StringBuffer(); // 字节数组转换为 十六进制数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			System.out.println("原始字符串=====>" + decrypt);
			System.out.println("生成token=====>" + (hexString.toString().toUpperCase()));
			return hexString.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * @fun 排序
	 * @param params
	 * @return
	 */
	public static List<String> keySort(Map<String, ? extends Object> params) {
		if (params == null) {
			return null;
		}
		List<String> keyList = new ArrayList<String>(params.keySet());
		Collections.sort(keyList);

		return keyList;
	}

	/**
	 * @fun 排序后转为字符串
	 * @param params
	 * @return
	 */
	public static String sortAndConvertString(Map<String, ? extends Object> params, boolean needCharacterFlg,
			boolean valueEncodeFlg) {
		if (params == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		List<String> keyList = new ArrayList<String>(params.keySet());
		Collections.sort(keyList);
		try {
			for (String s : keyList) {
				if (!"sign".equalsIgnoreCase(s) && !StringUtils.isEmpty(params.get(s))) {
					if (needCharacterFlg) {
						if (valueEncodeFlg) {
							sb.append(s + "=" + URLEncoder.encode(String.valueOf(params.get(s)), "utf-8") + "&");
						} else {
							sb.append(s + "=" + String.valueOf(params.get(s)) + "&");
						}
					} else {
						if (valueEncodeFlg) {
							sb.append(s + URLEncoder.encode(String.valueOf(params.get(s)), "utf-8"));
						} else {
							sb.append(s + String.valueOf(params.get(s)));
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return sb.toString();
		}

		return sb.toString();
	}

	public static String strToMD5Sign(String toSignStr, boolean toUpperFlg) {
		// 拼接要签名的字符串
		StringBuilder builder = new StringBuilder(toSignStr);
		LogUtil.writeMessage("MD5转换前字符串:" + builder);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(builder.toString().getBytes("utf-8"));
			builder.setLength(0);
			for (byte b : bytes) {
				String hx = Integer.toHexString(b & 0XFF);
				builder.append(hx.length() == 1 ? "0" + hx : hx);
			}
			if (toUpperFlg) {
				LogUtil.writeMessage("字符串MD5加密（大写）:" + builder.toString().toUpperCase());
				return builder.toString().toUpperCase();
			} else {
				LogUtil.writeMessage("字符串MD5加密（小写）:" + builder.toString().toLowerCase());
				return builder.toString().toLowerCase();
			}
		} catch (Exception e) {
			LogUtil.writeMessage("字符串MD5加密异常:" + e);
			return "字符串MD5加密异常";
		}
	}

	public static String strToBASE64Sign(String toSignStr) {
		// 拼接要签名的字符串
		StringBuilder builder = new StringBuilder(toSignStr);
		LogUtil.writeMessage("BASE64转换前字符串:" + builder);
		try {
			String resultSignStr = new String(Base64.encodeBase64(builder.toString().getBytes("utf-8")), "utf-8");
			LogUtil.writeMessage("字符串BASE64加密:" + resultSignStr);
			return resultSignStr;
		} catch (Exception e) {
			LogUtil.writeMessage("字符串BASE64加密异常:" + e);
			return "字符串BASE64加密异常";
		}
	}
}
