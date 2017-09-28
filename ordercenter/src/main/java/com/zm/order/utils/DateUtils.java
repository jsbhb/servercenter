package com.zm.order.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getTime(Integer type, Integer day){
		Calendar cal = Calendar.getInstance();
		cal.add(type, day);
		return sdf.format(cal.getTime());
	}
}
