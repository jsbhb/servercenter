package com.zm.supplier.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class SignUtil {

	public static String TianTianSign(String msg, String appSecret, String date){
		String toSignStr = msg+appSecret+date;
		String sign = Base64.encodeBase64(DigestUtils.md5(toSignStr)).toString().toUpperCase();
		try {
			return URLEncoder.encode(sign, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
