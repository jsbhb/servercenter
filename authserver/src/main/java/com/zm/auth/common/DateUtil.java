package com.zm.auth.common;

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

	/**
	 * 获取固定的时间 getFixDate:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author hebin
	 * @return
	 * @since JDK 1.7
	 */
	public static Date getFixDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, minute, second); // 年月日
																// 也可以具体到时分秒如calendar.set(2015,
		Date date = calendar.getTime();// date就是你需要的时间

		return date;
	}
}
