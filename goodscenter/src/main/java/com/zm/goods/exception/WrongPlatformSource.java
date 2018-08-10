package com.zm.goods.exception;

public class WrongPlatformSource extends Exception {

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;

	public WrongPlatformSource(String msg){
		super(msg);
	}
}
