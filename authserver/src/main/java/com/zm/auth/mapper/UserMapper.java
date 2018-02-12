package com.zm.auth.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zm.auth.model.PlatformUser;
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
	public UserInfo getUserByName(UserInfo userInfo);

	/**
	 * 
	 * getUserByPhone:根据电话号码获取用户。 <br/>
	 * 
	 * @author hebin
	 * @param phone
	 * @return
	 * @since JDK 1.7
	 */
	public UserInfo getUserByPhone(UserInfo userInfo);

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
	 * insert:插入新用户. <br/>
	 * 
	 * @author hebin
	 * @param PlatformUser
	 * @return
	 * @since JDK 1.7
	 */
	public void insertPlatformUser(@Param("set")Set<PlatformUser> list);

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
	public UserInfo getUserByPlatId(UserInfo userInfo);

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
	
	String getUserIdByUserName(String userName);
	
	String getUserIdByOpenId(String openId);
	
}
