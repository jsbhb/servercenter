package com.zm.order.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

public class RegularUtil {

	private static final String MOBILE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";

	private static final String ID_REGEX = "^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$";
	
	private static final String[] AREA_CODE = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44",
			"45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

	public final static boolean isPhone(String str) {
		if(str == null){
			return false;
		}
		if (str.matches(MOBILE_REGEX)) {
			return true;
		}
		return false;
	}

	public final static boolean isIdentify(String idStr) {
		if (idStr == null || (idStr.length() != 15 && idStr.length() != 18)) {
			return false;
		}
		if (idStr.length() == 15) {
			StringBuilder sb = new StringBuilder();
			sb.append(idStr.substring(0, 6)).append("19").append(idStr.substring(6));
			try {
				sb.append(getVerifyCode(sb.toString()));
				idStr = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(sb);
		}

		if(!idStr.matches(ID_REGEX)){
			return false;
		}
		
		char[] id = {};
		for (int i = 0; i < idStr.length(); i++) {
			id = Arrays.copyOf(id, id.length + 1);
			id[id.length - 1] = idStr.charAt(i);
		}
		if (!verify(id)) {
			return false;
		}
		// ================ 出生年月是否有效 ================
		String strYear = idStr.substring(6, 10);// 年份
		String strMonth = idStr.substring(10, 12);// 月份
		String strDay = idStr.substring(12, 14);// 日期
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			s.parse(strYear + "-" + strMonth + "-" + strDay);
		} catch (java.text.ParseException e) {// 类型转换异常
			return false;
		}
		
		

		// ================ 地区码是否有效 ================

		if (!ArrayUtils.contains(AREA_CODE, idStr.substring(0,2))) {
			// "身份证地区编码错误";
			return false;
		}
		return true;
	}

	/**
	 * 将已经加上年份的15位身份证，按照规则由17位推算出第18位
	 * 
	 * @param idCardNumber
	 * @return
	 */
	public final static char getVerifyCode(String idCardNumber) {
		char[] Ai = idCardNumber.toCharArray();
		int[] Wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] verifyCode = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int S = 0;
		int Y;
		for (int i = 0; i < Wi.length; i++) {
			S += (Ai[i] - '0') * Wi[i];
		}
		Y = S % 11;
		return verifyCode[Y];
	}

	public final static boolean verify(char[] id) {
		int sum = 0;
		int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		for (int i = 0; i < id.length - 1; i++) {
			sum += (id[i] - '0') * w[i];
		}
		int c = sum % 11;
		char code = ch[c];
		char last = id[id.length - 1];
		last = last == 'x' ? 'X' : last;
		return last == code;
	}
	
	public static void main(String[] args) {
		System.out.println(isIdentify("330206991111112"));
	}
}
