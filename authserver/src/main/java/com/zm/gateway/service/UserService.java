package com.zm.gateway.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.zm.gateway.model.UserInfo;

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
}
