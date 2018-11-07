package com.zm.pay.exception;

public class PayUtilException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String errorCode;

	public PayUtilException(String msg){
		super(msg);
	}
	
	public PayUtilException(String msg, String errorCode){
		super(msg);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
