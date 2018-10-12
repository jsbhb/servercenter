package com.zm.user.utils;

import java.lang.reflect.Field;

public class ParamReplaceUtil {

	private Object obj;
	
	public ParamReplaceUtil(Object clazz){
		this.obj = clazz;
	}
	
	public void paramReplace(String fieldName,String oldStr,String newStr){
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			if(field.getName().contains(fieldName)){
				try {
					field.setAccessible(true);
					String temPicPath = field.get(obj)== null? "":field.get(obj).toString();
					temPicPath = temPicPath.replace(oldStr, newStr);
					field.set(obj, temPicPath);
//					String tmpPicPath = clazz.getMethod("get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1, field.getName().length())).invoke(obj).toString();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
