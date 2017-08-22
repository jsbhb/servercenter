package com.zm.user.utils;

import com.zm.user.constants.Constants;

public class RegularUtil {

	private static final String EMAIL_REGEX = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";
	private static final String MOBILE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";

	public static String emailOrPhone(String str) {
		if (str.matches(EMAIL_REGEX)) {
			return Constants.EMAIL;
		}
		if (str.matches(MOBILE_REGEX)) {
			return Constants.MOBILE;
		}
		return Constants.ACCOUNT;
	}

	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim())) {
			return s.matches("^[0-9]*$");
		} else {
			return false;
		}
	}
}
