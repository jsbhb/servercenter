package com.zm.gateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**  
 * ClassName: ResultPojo <br/>  
 * Function: 返回统一对象. <br/>   
 * date: Aug 11, 2017 1:55:04 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
@JsonInclude(Include.NON_NULL)
public class ResultPojo {

	private String errorCode;
	private String errorMsg;
	private boolean success;
	private Object obj;
	
	public ResultPojo(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.success = false;
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
	
	
}
