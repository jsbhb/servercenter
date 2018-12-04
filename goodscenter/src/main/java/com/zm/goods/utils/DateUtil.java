package com.zm.goods.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @fun 日期工具类
 * @author wqy
 *
 * @date 2017年7月10日
 */
public class DateUtil {
	
	public static long stringToLong(String strTime, String formatType) throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static Date stringToDate(String strTime, String formatType) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			if(strTime == null){
				return new Date();
			}
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			date = new Date();
		}
		return date;
	}
	
	public static String getNowTimeStr(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		return sdf.format(date);
	}
	
	/**
	 * @fun 返回现在是不是在指定时间之前
	 * @param startTime
	 * @param duration
	 * @return
	 * @throws ParseException
	 */
	public static boolean isAfter(String startTime, int duration) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar appointTime = Calendar.getInstance();
		appointTime.setTime(sdf.parse(startTime));
		appointTime.add(Calendar.HOUR_OF_DAY, duration);//获取指定时间
		Calendar now = Calendar.getInstance();
		return now.after(appointTime);
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println(isAfter(getNowTimeStr("yyyy-MM-dd HH:mm:ss"), 1));
	}
}
