package com.zm.log.pojo;

/**  
 * ClassName: LogInfo <br/>  
 * Function: 错误日志类POJO. <br/>   
 * date: Aug 16, 2017 10:14:43 AM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public class ExceptionLog extends Log{

	private String opt;
	
	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

}
