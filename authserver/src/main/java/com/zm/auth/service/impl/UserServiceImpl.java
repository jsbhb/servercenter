package com.zm.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zm.auth.mapper.UserMapper;
import com.zm.auth.model.SecurityUserFactory;
import com.zm.auth.model.UserInfo;
import com.zm.auth.service.UserService;

/**
 * 
 * ClassName: UserServiceImpl <br/>
 * Function: security 实现UserDetailService接口. <br/>
 * date: Aug 21, 2017 7:27:33 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	// @Autowired
	// private RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserInfo userInfo = getUserByName(userName);
		List<String> roles = new ArrayList<String>();

		if (userInfo == null) {
			throw new UsernameNotFoundException(userName);
		}

		roles.add("ADMIN");
		roles.add("USER");
		userInfo.setAuthorities(roles);

		return SecurityUserFactory.create(userInfo);

		// //根据用户获取用户角色
		// List<Role> roles = roleService.getUserRole(userInfo.getUserId());
		// //定义权限集合
		// List<SimpleGrantedAuthority> grantedAuthorities = new
		// ArrayList<SimpleGrantedAuthority>();
		// //添加权限到集合中
		// for (Role role : roles){
		// grantedAuthorities.add(new
		// SimpleGrantedAuthority("ROLE_"+role.getRoleType()));
		// }
		// boolean booleanStatus = true;
		// if(userInfo.getStatus() == 0){
		// booleanStatus = false;
		// }
		// //加密密码
		// BCryptPasswordEncoder bCryptPasswordEncoder = new
		// BCryptPasswordEncoder(16);
		// User user = new
		// User(userInfo.getUserName(),bCryptPasswordEncoder.encode(userInfo.getPassword()),booleanStatus,true,true,
		// true, grantedAuthorities);
		// return user;
	}

	@Override
	public UserDetails loadUserByPlatId(String platId) throws UsernameNotFoundException {

		UserInfo userInfo = getUserByName(platId);
		List<String> roles = new ArrayList<String>();

		if (userInfo == null) {
			throw new UsernameNotFoundException(platId);
		}

		roles.add("ADMIN");
		roles.add("USER");
		userInfo.setAuthorities(roles);

		return SecurityUserFactory.create(userInfo);
	}

	@Override
	public UserInfo getUserByName(String userName) {
		UserInfo userInfo = userMapper.getUserByName(userName);
		return userInfo;
	}

	@Override
	public UserInfo getUserByPhone(String phone) {
		UserInfo userInfo = userMapper.getUserByPhone(phone);
		return userInfo;
	}

	@Override
	public UserInfo getUserByPlatId(String platId) {
		UserInfo userInfo = userMapper.getUserByPlatId(platId);
		return userInfo;
	}
}
