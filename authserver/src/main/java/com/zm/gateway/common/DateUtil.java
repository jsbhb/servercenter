package com.zm.gateway.common;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * ClassName: DateUtil <br/>
 * Function: 时间工具包. <br/>
 * date: Aug 21, 2017 6:54:19 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class DateUtil {
	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
}
