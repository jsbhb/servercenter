package com.zm.order.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * @fun 获取给定时间加上或减去给定时间后的值
	 * @param type
	 * @param day
	 * @return
	 */
	public static String getTime(String time, String format, Integer type, Integer day) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(type, day);
		return sdf.format(cal.getTime());
	}

	/**
	 * @fun 获取当前时间加上或减去给定时间后的值 根据指定格式返回
	 * @param type
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getTime(Integer type, Integer day, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(type, day);
		return sdf.format(cal.getTime());
	}

	/**
	 * @fun 获取当前时间给定格式的字符串
	 * @return
	 */
	public static String getTimeString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * @fun 给定时间和格式，转成新格式字符串
	 * @param time
	 * @param oldFormat
	 * @param newFormat
	 * @return
	 */
	public static String getTimeString(String time, String oldFormat, String newFormat) {
		SimpleDateFormat oldsdf = new SimpleDateFormat(oldFormat);
		SimpleDateFormat newsdf = new SimpleDateFormat(newFormat);
		try {
			return newsdf.format(oldsdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @fun 判断给定时间是不是今天
	 * @param time
	 * @param format
	 * @return
	 */
	public static boolean judgeTime(String time, String format) {
		SimpleDateFormat oldsdf = new SimpleDateFormat(format);
		SimpleDateFormat newsdf = new SimpleDateFormat("yyyyMMdd");
		try {
			String oldTime = newsdf.format(oldsdf.parse(time));
			String newTime = newsdf.format(new Date());
			return oldTime.equals(newTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @fun 计算给定时间和现在差几个月
	 * @param dateStr
	 * @return
	 */
	public static int compareWithNow(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String nowYearMonth = sdf.format(new Date());
		Calendar afferent = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		try {
			afferent.setTime(sdf.parse(dateStr));
			now.setTime(sdf.parse(nowYearMonth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year = (now.get(Calendar.YEAR) - afferent.get(Calendar.YEAR)) * 12;
		int month = now.get(Calendar.MONTH) - afferent.get(Calendar.MONTH);
		return Math.abs(year + month);
	}

	public static boolean judgeDateFormat(String str, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			formatter.setLenient(false);
			formatter.parse(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @fun 判断给定时间和今天差几天,并且给定时间必须在当前时间之前
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());

		Calendar cal2 = Calendar.getInstance();
		try {
			cal2.setTime(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) {// 不同一年
			if(year1 < year2){
				throw new RuntimeException("时间必须比今天早");
			}
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {// 闰年
					timeDistance += 366;
				} else {// 不是闰年
					timeDistance += 365;
				}
			}

			return timeDistance + (day1 - day2);
		} else {// 同年
			if(day1 < day2){
				throw new RuntimeException("时间必须比今天早");
			}
			return day1 - day2;
		}
	}
}
