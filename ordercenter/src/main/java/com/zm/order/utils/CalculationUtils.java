package com.zm.order.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtils {
	/**
	 * 提供精确加法计算的add方法
	 * 
	 * @param value1
	 *            被加数
	 * @param value2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		return b1.add(b2).doubleValue();
	}

	public static double add(double value1, double value2, double value3) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		BigDecimal b3 = new BigDecimal(String.valueOf(value3));
		return b1.add(b2).add(b3).doubleValue();
	}

	/**
	 * 提供精确减法运算的sub方法
	 * 
	 * @param value1
	 *            被减数
	 * @param value2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确乘法运算的mul方法
	 * 
	 * @param value1
	 *            被乘数
	 * @param value2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的除法运算方法div
	 * 
	 * @param value1
	 *            被除数
	 * @param value2
	 *            除数
	 * @param scale
	 *            精确范围
	 * @return 两个参数的商
	 * @throws IllegalAccessException
	 */
	public static double div(double value1, double value2, int scale) {
		// 如果精确范围小于0，抛出异常信息
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的除法运算方法div
	 * 
	 * @param value1
	 *            被除数
	 * @param value2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));
		return b1.divide(b2).doubleValue();
	}

	public static double round(int scale, double value) {
		BigDecimal b = new BigDecimal(String.valueOf(value));
		return b.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}
}
