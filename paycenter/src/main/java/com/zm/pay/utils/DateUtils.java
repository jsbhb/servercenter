package com.zm.pay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @fun 判断传入的日期和当前时间是否超过了传入的指定大小 (传入时间格式为“yyyy-MM-dd HH:mm:ss”)
	 * @return 超过返回true；没超过返回false；
	 */
	public static boolean judgeDate(String startTime, Long time) {
		Date now = new Date();
		Long nowTime = now.getTime();
		Long start = 0L;
		try {
			Date startDate = sdf.parse(startTime);
			start = startDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("时间格式不对");
		}
		if (nowTime - start >= time) {
			return true;
		} else {
			return false;
		}
	}
}
