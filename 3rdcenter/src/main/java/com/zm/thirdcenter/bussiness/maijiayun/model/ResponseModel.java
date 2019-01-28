package com.zm.thirdcenter.bussiness.maijiayun.model;

public class ResponseModel {
	
	private boolean isOk;

	private String errorCode;

	private String errorMsg;

	private String resolveMsg;

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
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

	public String getResolveMsg() {
		return resolveMsg;
	}

	public void setResolveMsg(String resolveMsg) {
		this.resolveMsg = resolveMsg;
	}
	
}