package com.zm.supplier.util;

import java.text.SimpleDateFormat;
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
}
