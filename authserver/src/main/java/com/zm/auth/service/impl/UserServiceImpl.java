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
	public UserDetails loadUserByUsername(UserInfo user) throws UsernameNotFoundException {

		String userName = user.getUserName();
		UserInfo userInfo = null;
		if (userName.endsWith(WX_OPENID_SECRET)) {
			user.setOpenId((userName.split(",")[0]));
			userInfo = getUserByOpenId(user);
		} else {
			userInfo = getUserByName(user);
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
	public UserInfo getUserByOpenId(UserInfo userInfo) {
		return userMapper.queryByOpenId(userInfo);
	}

	@Override
	public UserDetails loadUserByPlatId(UserInfo user) throws UsernameNotFoundException {

		UserInfo userInfo = getUserByPlatId(user);
		List<String> roles = new ArrayList<String>();

		if (userInfo == null) {
			throw new UsernameNotFoundException(user.getUserId());
		}

		roles.add("ADMIN");
		roles.add("USER");
		userInfo.setAuthorities(roles);

		return SecurityUserFactory.create(userInfo);
	}

	@Override
	public UserInfo getUserByName(UserInfo userInfo) {
		return userMapper.getUserByName(userInfo);
	}

	@Override
	public UserInfo getUserByPhone(UserInfo userInfo) {
		return userMapper.getUserByPhone(userInfo);
	}

	@Override
	public UserInfo getUserByPlatId(UserInfo userInfo) {
		return userMapper.getUserByPlatId(userInfo);
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
