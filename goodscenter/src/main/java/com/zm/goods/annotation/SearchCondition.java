package com.zm.goods.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @lucene 搜索条件还是过滤搜索
 * @author user
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchCondition {

	public static final int SEARCH = 0;
	public static final int FILTER = 1;
	
	int value();//0搜索条件，1过滤条件
}
