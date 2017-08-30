package com.zm.goods.utils;

public class CommonUtils {

	public static Double getMinDouble(double v1, double v2){
		if(v1 > v2){
			return v2;
		}
		return v1;
	}
	
	public static Double getMaxDouble(double v1, double v2){
		if(v1 > v2){
			return v1;
		}
		return v2;
	}
}
