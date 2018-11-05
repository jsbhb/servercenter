package com.zm.pay.exception;

public class PayUtilException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private int errorCode;

	public PayUtilException(String msg){
		super(msg);
	}
	
	public PayUtilException(String msg, int errorCode){
		super(msg);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
