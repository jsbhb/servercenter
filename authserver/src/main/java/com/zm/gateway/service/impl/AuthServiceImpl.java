package com.zm.gateway.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.gateway.common.JWTUtil;
import com.zm.gateway.mapper.UserMapper;
import com.zm.gateway.model.SecurityUserDetail;
import com.zm.gateway.model.SecurityUserFactory;
import com.zm.gateway.model.UserInfo;
import com.zm.gateway.service.AuthService;

/**
 * 
 * ClassName: AuthServiceImpl <br/>
 * Function: 权限服务实现类. <br/>
 * date: Aug 21, 2017 8:11:01 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */

@Service
public class AuthServiceImpl implements AuthService {

	private UserMapper userMapper;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	public AuthServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Transactional
	@Override
	public SecurityUserDetail register(UserInfo userInfo) {

		String userName = userInfo.getUserName();
		if (userMapper.getUserByName(userName) != null) {
			return null;
		}
		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// final String rawPassword = userInfo.getPassword();
		// userInfo.setPassword(encoder.encode(rawPassword));
		userInfo.setLastPasswordResetDate(new Date());
		userInfo.setAuthorities(asList("ROLE_USER"));
		userMapper.insert(userInfo);

		SecurityUserDetail userDetail = SecurityUserFactory.create(userInfo);

		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.PASSWORD, userInfo.getPassword());
		claim.put(JWTUtil.USER_NAME, userInfo.getUserName());
		userDetail.setToken(JWTUtil.generateToken(claim));

		return userDetail;
	}

	private List<String> asList(String role) {
		List<String> roles = new ArrayList<String>();
		roles.add(role);
		return roles;
	}

	@Override
	public SecurityUserDetail login(UserInfo userInfo) {
		// UsernamePasswordAuthenticationToken upToken = new
		// UsernamePasswordAuthenticationToken(username, password);
		// Authentication authentication =
		// authenticationManager.authenticate(upToken);
		// SecurityContextHolder.getContext().setAuthentication(authentication);

		userInfo = userMapper.getUserForLogin(userInfo);

		if (userInfo.getUserId() == null || "".equals(userInfo.getUserId())) {
			return null;
		}

		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.PASSWORD, userInfo.getPassword());
		claim.put(JWTUtil.USER_NAME, userInfo.getUserName());
		userInfo.setAuthorities(asList("ROLE_USER"));

		SecurityUserDetail userDetail = SecurityUserFactory.create(userInfo);

		userDetail.setToken(JWTUtil.generateToken(claim));

		return userDetail;
	}

	@Override
	public String refresh(String oldToken) {
		// final String token = oldToken.substring(tokenHead.length());
		// String username = JWTUtil.getUsernameFromToken(token);
		// SecurityUserDetail user = (SecurityUserDetail)
		// userDetailsService.loadUserByUsername(username);
		// if (JWTUtil.canTokenBeRefreshed(token,
		// user.getLastPasswordResetDate())) {
		// return JWTUtil.refreshToken(token);
		// }
		return null;

	}

}
