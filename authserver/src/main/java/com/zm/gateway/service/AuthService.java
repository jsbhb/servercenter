package com.zm.gateway.service;

import com.zm.gateway.model.UserInfo;

/**
 * 
 * ClassName: AuthService <br/>
 * Function: 权限服务. <br/>
 * date: Aug 21, 2017 8:09:12 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public interface AuthService {
	/**
	 * 
	 * register:用户注册. <br/>  
	 *  
	 * @author hebin  
	 * @param userInfo
	 * @return  
	 * @since JDK 1.7
	 */
	UserInfo register(UserInfo userInfo);
	
	
	/**
	 * 
	 * login:用户登录. <br/>  
	 *  
	 * @author hebin  
	 * @param username
	 * @param password
	 * @return  
	 * @since JDK 1.7
	 */
	String login(String username, String password);
	
	
	/**
	 * 
	 * refresh:用户刷新. <br/>  
	 *  
	 * @author hebin  
	 * @param oldToken
	 * @return  
	 * @since JDK 1.7
	 */
	String refresh(String oldToken);
	
}
