package com.zm.thirdcenter.utils;

/**
 * ClassName: RetryUtils <br/>
 * Function: 异常重试. <br/>
 * date: Aug 18, 2017 4:01:15 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class RetryUtils {

	/**
	 * 回调结果检查
	 */
	public interface ResultCheck {
		boolean matching();

		String getJson();
	}

	/**
	 * 在遇到异常时尝试重试
	 * 
	 * @param retryLimit
	 *            重试次数
	 * @param retryCallable
	 *            重试回调
	 * @param <V>
	 *            泛型
	 * @return V 结果
	 */
	public static <V extends ResultCheck> V retryOnException(int retryLimit,
			java.util.concurrent.Callable<V> retryCallable) {

		V v = null;
		for (int i = 0; i < retryLimit; i++) {
			try {
				v = retryCallable.call();
			} catch (Exception e) {
			}
			if (null != v && v.matching())
				break;
		}
		return v;
	}

	/**
	 * 在遇到异常时尝试重试
	 * 
	 * @param retryLimit
	 *            重试次数
	 * @param sleepMillis
	 *            每次重试之后休眠的时间
	 * @param retryCallable
	 *            重试回调
	 * @param <V>
	 *            泛型
	 * @return V 结果
	 * @throws java.lang.InterruptedException
	 *             线程异常
	 */
	public static <V extends ResultCheck> V retryOnException(int retryLimit, long sleepMillis,
			java.util.concurrent.Callable<V> retryCallable) throws java.lang.InterruptedException {

		V v = null;
		for (int i = 0; i < retryLimit; i++) {
			try {
				v = retryCallable.call();
			} catch (Exception e) {
			}
			if (null != v && v.matching())
				break;
			Thread.sleep(sleepMillis);
		}
		return v;
	}

}
