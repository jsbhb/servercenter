package com.zm.user.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getNow(){
		return sdf.format(new Date());
	}
}
