package com.zm.order.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**  
 * ClassName: ResultPojo <br/>  
 * Function: 返回统一对象. <br/>   
 * date: Aug 11, 2017 1:55:04 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
@ApiModel
@JsonInclude(Include.NON_NULL)
public class ResultModel {

	@ApiModelProperty(value="错误码")
	private String errorCode;
	@ApiModelProperty(value="错误信息")
	private String errorMsg;
	@ApiModelProperty(value="是否成功")
	private boolean success;
	@ApiModelProperty(value="返回对象")
	private Object obj;
	
	public ResultModel(){}
	
	public ResultModel(boolean success, Object obj){
		this.success = success;
		this.obj = obj;
	}
	
	public ResultModel(boolean success, String errorMsg){
		this.success = success;
		this.errorMsg = errorMsg;
	}
	
	public ResultModel(boolean success, String errorCode, String errorMsg){
		this.success = success;
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
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
