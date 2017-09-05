package com.zm.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${wx_openId_secret}")
	private String WX_OPENID_SECRET;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserInfo userInfo = null;
		if (userName.endsWith(WX_OPENID_SECRET)) {
			userInfo = getUserByOpenId(userName.split("_")[0]);
		} else {
			userInfo = getUserByName(userName);
		}

		if (userInfo == null) {
			throw new UsernameNotFoundException(userName);
		}
		List<String> roles = new ArrayList<String>();

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

	/**
	 * getUserByOpenId:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author hebin
	 * @param string
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public UserInfo getUserByOpenId(String openId) {
		return userMapper.queryByOpenId(new UserInfo(openId));
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
		return userMapper.getUserByName(userName);
	}

	@Override
	public UserInfo getUserByPhone(String phone) {
		return userMapper.getUserByPhone(phone);
	}

	@Override
	public UserInfo getUserByPlatId(String platId) {
		return userMapper.getUserByPlatId(platId);
	}
}
