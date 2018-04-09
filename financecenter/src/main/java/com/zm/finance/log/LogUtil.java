package com.zm.finance.log;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName LogUtil
 * @Description 日志工具类
 */
public class LogUtil {

	private final static Logger FINANCELOG = LoggerFactory.getLogger("FINANCE_LOG");

	final static String INFO_LOG = "============================== 【INFO_LOG】 ==============================";
	final static String ERROR_LOG = "==============================  【ERROR_LOG】  ==============================";
	final static String DEBUG_LOG = "============================== 【DEBUG_LOG】 ==============================";

	final static String LOG_STRING_REQ_MSG_BEGIN = "============================== SDK REQ MSG BEGIN ==============================";
	final static String LOG_STRING_REQ_MSG_END = "==============================  SDK REQ MSG END  ==============================";
	final static String LOG_STRING_RSP_MSG_BEGIN = "============================== SDK RSP MSG BEGIN ==============================";
	final static String LOG_STRING_RSP_MSG_END = "==============================  SDK RSP MSG END  ==============================";

	/**
	 * 记录普通日志
	 * 
	 * @param cont
	 */
	public static void writeLog(String cont) {
		FINANCELOG.info(INFO_LOG + cont);
	}

	/**
	 * 记录ERORR日志
	 * 
	 * @param cont
	 */
	public static void writeErrorLog(String cont) {
		FINANCELOG.error(ERROR_LOG + cont);
	}

	/**
	 * 记录ERROR日志
	 * 
	 * @param cont
	 * @param ex
	 */
	public static void writeErrorLog(String cont, Throwable ex) {
		FINANCELOG.error(ERROR_LOG + cont, ex);
	}

	/**
	 * 记录通信报文
	 * 
	 * @param msg
	 */
	public static void writeMessage(String msg) {
		FINANCELOG.info(msg);
	}

	/**
	 * 打印请求报文
	 * 
	 * @param reqParam
	 */
	public static void printRequestLog(Map<String, String> reqParam) {
		writeMessage(LOG_STRING_REQ_MSG_BEGIN);
		Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			writeMessage("[" + en.getKey() + "] = [" + en.getValue() + "]");
		}
		writeMessage(LOG_STRING_REQ_MSG_END);
	}

	/**
	 * 打印响应报文.
	 * 
	 * @param res
	 */
	public static void printResponseLog(String res) {
		writeMessage(LOG_STRING_RSP_MSG_BEGIN);
		writeMessage(res);
		writeMessage(LOG_STRING_RSP_MSG_END);
	}

	/**
	 * debug方法
	 * 
	 * @param cont
	 */
	public static void debug(String cont) {
		if (FINANCELOG.isDebugEnabled()) {
			FINANCELOG.debug(DEBUG_LOG + cont);
		}
	}
}
