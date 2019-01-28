package com.zm.thirdcenter.exception;

public class WxCodeException extends Exception{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	
	public WxCodeException(int code,String errMsg){
		super(errMsg);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
