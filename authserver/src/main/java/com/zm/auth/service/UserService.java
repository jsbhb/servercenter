package com.zm.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zm.auth.model.UserInfo;

/**
 * @author
 * @version 1.0 2016/10/11
 * @description
 */
public interface UserService extends UserDetailsService {

	/**
	 * 根据用户名获取用户
	 * 
	 * @param userName
	 * @return
	 */
	public UserInfo getUserByName(String userName);

	/**
	 * 根据电话号码获取用户
	 * 
	 * @param userName
	 * @return
	 */
	public UserInfo getUserByPhone(String phone);

	/**
	 * loadUserByPlatId:根据用户编号获取用户. <br/>
	 * 
	 * @author hebin
	 * @param platId
	 * @return
	 * @throws UsernameNotFoundException
	 * @since JDK 1.7
	 */
	UserDetails loadUserByPlatId(String platId) throws UsernameNotFoundException;

	/**
	 * getUserByPlatId:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param platId
	 * @return
	 * @since JDK 1.7
	 */
	UserInfo getUserByPlatId(String platId);

	/**
	 * getUserByOpenId:根据用户openid获取用户信息. <br/>
	 * 
	 * @author hebin
	 * @param openId
	 * @return
	 * @since JDK 1.7
	 */
	UserInfo getUserByOpenId(String openId);
}
