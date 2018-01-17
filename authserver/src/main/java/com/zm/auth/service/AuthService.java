package com.zm.auth.service;

import com.zm.auth.model.SecurityUserDetail;
import com.zm.auth.model.UserInfo;

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
	SecurityUserDetail register(UserInfo userInfo);
	
	
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
	SecurityUserDetail login(UserInfo userInfo);
	
	
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
	
	/**
	 * 
	 * check:检验用户名. <br/>  
	 *  
	 * @author hebin  
	 * @param oldToken
	 * @return  
	 * @since JDK 1.7
	 */
	boolean checkAccount(UserInfo userInfo);


	boolean modifyPwd(UserInfo userInfo);


	boolean createAccount(Integer userId, Integer platUserType);
	
}
