package com.zm.order.exception;

public class ParameterException extends Exception{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;

	public ParameterException(String errorMsg){
		super(errorMsg);
	}
}
