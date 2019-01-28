package com.zm.thirdcenter.utils;

import java.util.Random;

public class CommonUtil {

	private static Random ra = new Random();
	
	public static String getPhoneCode(){
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<6;i++){
			sb.append(ra.nextInt(10));
		}
		
		return sb.toString();
	}
}
