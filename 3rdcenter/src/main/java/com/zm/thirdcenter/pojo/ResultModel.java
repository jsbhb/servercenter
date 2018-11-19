package com.zm.thirdcenter.pojo;

/**
 * ClassName: ResultPojo <br/>
 * Function: 返回统一对象. <br/>
 * date: Aug 11, 2017 1:55:04 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class ResultModel {

	private String errorCode;
	private String errorMsg;
	private boolean success;
	private Object obj;

	public ResultModel(boolean flag, String msg) {
		this.success = flag;
		this.errorMsg = msg;
	}

	public ResultModel(boolean flag, String msg,String errorCode){
		this.success = flag;
		this.errorMsg = msg;
		this.errorCode = errorCode;
	}

	public ResultModel(Object obj) {
		this.success = true;
		this.obj = obj;
	}

	public ResultModel() {
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "ResultModel [errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", success=" + success + ", obj="
				+ obj + "]";
	}

}
