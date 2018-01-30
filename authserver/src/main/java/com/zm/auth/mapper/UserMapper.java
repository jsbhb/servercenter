package com.zm.auth.mapper;

import com.zm.auth.model.UserInfo;

/**
 * @author
 * @version 1.0 2016/10/11
 * @description
 */
public interface UserMapper {

	/**
	 * 
	 * getUserByName:根据用户名获取用户。 <br/>
	 * 
	 * @author hebin
	 * @param userName
	 * @return
	 * @since JDK 1.7
	 */
	public UserInfo getUserByName(String userName);

	/**
	 * 
	 * getUserByPhone:根据电话号码获取用户。 <br/>
	 * 
	 * @author hebin
	 * @param phone
	 * @return
	 * @since JDK 1.7
	 */
	public UserInfo getUserByPhone(String phone);

	/**
	 * 
	 * insert:插入新用户. <br/>
	 * 
	 * @author hebin
	 * @param userInfo
	 * @return
	 * @since JDK 1.7
	 */
	public void insert(UserInfo userInfo);

	/**
	 * 
	 * getUserForLogin：登录校验. <br/>  
	 *  
	 * @author hebin  
	 * @param userName
	 * @param phone
	 * @param openId
	 * @param password
	 * @return  
	 * @since JDK 1.7
	 */
	public UserInfo getUserForLogin(UserInfo userInfo);

	/**  
	 * getUserByPlatId:根据平台用户编号查询用户. <br/>  
	 *  
	 * @author hebin  
	 * @param userId
	 * @return  
	 * @since JDK 1.7  
	 */
	public UserInfo getUserByPlatId(String userId);

	/**
	 * 
	 * queryByOpenId:根据openId查询用户 <br/>  
	 *  
	 * @author hebin  
	 * @param userInfo
	 * @return  
	 * @since JDK 1.7
	 */
	public UserInfo queryByOpenId(UserInfo userInfo);
	
	/**
	 * 
	 * modifyPwd:修改密码 <br/>  
	 *  
	 * @author wqy  
	 * @param userInfo
	 * @return  
	 * @since JDK 1.7
	 */
	int modifyPwd(UserInfo userInfo);
	
	
	/**
	 * 
	 * updateUserAuth:账号状态升级为可以登录订货平台 <br/>  
	 *  
	 * @author wqy  
	 * @param userName
	 * @since JDK 1.7
	 */
	void updateUserAuth(String userName);
	
	/**
	 * 
	 * insert2B：生成订货平台账号（同时可以登录普通平台）. <br/>  
	 *  
	 * @author hebin  
	 * @param userInfo
	 * @since JDK 1.7
	 */
	public void insert2B(UserInfo userInfo);
	
	/**
	 * 
	 * loginFor2B：登录订货平台. <br/>  
	 *  
	 * @author hebin  
	 * @param userInfo
	 * @since JDK 1.7
	 */
	UserInfo loginFor2B(UserInfo userInfo);

}
