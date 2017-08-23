package com.zm.gateway.mapper;

import com.zm.gateway.model.UserInfo;

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
	public UserInfo insert(UserInfo userInfo);

}
