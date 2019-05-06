package com.zm.supplier.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getDateString(Date data, String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(data);
		} catch (Exception e) {
			throw new RuntimeException("转换出错");
		}
	}
	/**
	 * @fun 获取指定时间
	 * @param date 
	 * @param i 加几天/几月/几年
	 * @param type 天/月/年
	 * @return
	 */
	public static Date getSpecifiedTime(Date date,int i, int type){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, i);
		return cal.getTime();
	}
	
	public static Date getDateByString(String str, String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(str);
		} catch (Exception e) {
			throw new RuntimeException("转换出错");
		}
	}

}
