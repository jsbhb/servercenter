package com.zm.finance.pojo;


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
	
	public ResultModel(){}
	
	public ResultModel(boolean flag){
		this.success = flag;
	}
	
	public ResultModel(boolean flag, Object obj){
		this.obj = obj;
		this.success = flag;
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
