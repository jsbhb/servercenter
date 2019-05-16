package com.zm.thirdcenter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String dataformat(String data, String oldFormat, String newFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		SimpleDateFormat newSdf = new SimpleDateFormat(newFormat);
		Date date = null;
		try {
			date = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newSdf.format(date);
	}

	public static String dataformat(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
}
