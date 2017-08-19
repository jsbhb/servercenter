package com.zm.log.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.log.pojo.LogInfo;

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
	 * saveLog:保存日志. <br/>  
	 *  
	 * @author wqy  
	 * @param info  
	 * @since JDK 1.7  
	 */
	void saveLog(LogInfo info);
	
	/**  
	 * listLogInfo:根据条件获取日志信息. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	List<LogInfo> listLogInfo(Map<String,Object> param);
	
	
	/**  
	 * removeLogInfo:根据时间删除之前日志信息. <br/>  
	 *  
	 * @author wqy  
	 * @param endTime  
	 * @since JDK 1.7  
	 */
	void removeLogInfo(String endTime);
}
