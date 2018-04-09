package com.zm.goods.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

	public static final int FRONT_PLAT = 1;//前端
	public static final int BACK_PLAT = 0;//后台
	public static final int ADD = 0;//新增
	public static final int DELETE = 1;//删除
	public static final int MODIFY = 2;//修改
	
	int source(); 
	int type();
	String content();
}
