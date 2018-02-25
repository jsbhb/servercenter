package com.zm.log.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.log.pojo.ExceptionLog;
import com.zm.log.pojo.LogInfo;
import com.zm.log.pojo.OpenInfLog;

/**  
 * ClassName: LogService <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 16, 2017 10:14:05 AM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public interface LogService {

	/**  
	 * saveLog:保存错误日志. <br/>  
	 *  
	 * @author wqy  
	 * @param info  
	 * @since JDK 1.7  
	 */
	void saveExceptionLog(ExceptionLog info);
	
	/**  
	 * listLogInfo:根据条件获取错误日志信息. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	List<ExceptionLog> listLogInfo(Map<String,Object> param);
	
	
	/**  
	 * removeLogInfo:根据时间删除之前日志信息. <br/>  
	 *  
	 * @author wqy  
	 * @param endTime  
	 * @since JDK 1.7  
	 */
	void removeLogInfo(String endTime);

	/**  
	 * saveOpenInfoLog:保存开放接口日志. <br/>  
	 *  
	 * @author wqy  
	 * @param log  
	 * @since JDK 1.7  
	 */
	void saveOpenInfoLog(OpenInfLog log);

	/**  
	 * saveLogInfo:保存自定义接口日志. <br/>  
	 *  
	 * @author wqy  
	 * @param log  
	 * @since JDK 1.7  
	 */
	void saveLogInfo(LogInfo logInfo);
}
