package com.zm.user.pojo;

public enum BackManagerErrorEnum {

	DELETE_ERROR("40010","还有其他地方正在使用该属性");
	
	private String errorCode;
	private String errorMsg;
	
	private BackManagerErrorEnum(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
