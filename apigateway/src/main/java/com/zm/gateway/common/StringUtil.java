package com.zm.gateway.common;

/**
 * 
 * ClassName: StringUtil <br/>
 * Function: 字符串工具类. <br/>
 * date: Aug 25, 2017 11:34:05 AM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class StringUtil {

	/**
	 * 
	 * checkNull:字符串判断空<br/>
	 * 
	 * @author hebin
	 * @param str
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean checkNull(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

}
