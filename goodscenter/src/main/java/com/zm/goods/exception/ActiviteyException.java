package com.zm.goods.exception;

public class ActiviteyException extends Exception{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.7  
	 */
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	
	public ActiviteyException(String errorMsg, int errorCode){
		super(errorMsg);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
