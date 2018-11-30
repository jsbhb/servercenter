package com.zm.goods.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Number {

	private String initNum;

	public Number(String initNum) {
		this.initNum = initNum;
	}

	public Number sub(String value) {
		BigDecimal b1 = new BigDecimal(this.getInitNum());
		BigDecimal b2 = new BigDecimal(value);
		return new Number(b1.subtract(b2).toString());
	}

	public Number Invert() {
		BigDecimal b1 = new BigDecimal(this.getInitNum());
		BigDecimal b2 = new BigDecimal("0");
		return new Number(b2.subtract(b1).toString());
	}

	public Number mul(String value) {
		BigDecimal b1 = new BigDecimal(this.getInitNum());
		BigDecimal b2 = new BigDecimal(value);
		return new Number(b1.multiply(b2).toString());
	}

	public Number add(String value) {
		BigDecimal b1 = new BigDecimal(this.getInitNum());
		BigDecimal b2 = new BigDecimal(value);
		return new Number(b1.add(b2).toString());
	}

	public Number div(String value, int scale) throws IllegalAccessException {
		// 如果精确范围小于0，抛出异常信息
		if (scale < 0) {
			throw new IllegalAccessException("精确度不能小于0");
		}
		BigDecimal b1 = new BigDecimal(this.getInitNum());
		BigDecimal b2 = new BigDecimal(value);
		String result = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
		return new Number(result);
	}

	public Number div(String value) {
		BigDecimal b1 = new BigDecimal(this.getInitNum());
		BigDecimal b2 = new BigDecimal(value);
		return new Number(b1.divide(b2).toString());
	}

	public Number round(int scale) {
		BigDecimal b = new BigDecimal(String.valueOf(initNum));
		return new Number(b.setScale(scale, RoundingMode.HALF_UP).toString());
	}

	public String parseToString() {
		return initNum;
	}

	public Long parseToLong() {
		return Long.valueOf(round(0).getInitNum());
	}
	
	public double parseToDouble() {
		return Double.valueOf(initNum);
	}

	public String getInitNum() {
		return initNum;
	}

	public void setInitNum(String initNum) {
		this.initNum = initNum;
	}
}
