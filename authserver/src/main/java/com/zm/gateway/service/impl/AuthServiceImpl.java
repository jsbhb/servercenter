package com.zm.gateway.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.zm.gateway.common.JWTUtil;
import com.zm.gateway.mapper.UserMapper;
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

	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;
	private UserMapper userMapper;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			UserMapper userMapper) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.userMapper = userMapper;
	}

	@Override
	public String register(UserInfo userInfo) {

		final String userName = userInfo.getUserName();
		if (userMapper.getUserByName(userName) != null) {
			return null;
		}
		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// final String rawPassword = userInfo.getPassword();
		// userInfo.setPassword(encoder.encode(rawPassword));
		userInfo.setLastPasswordResetDate(new Date());
		userInfo.setAuthorities(asList("ROLE_USER"));
		userMapper.insert(userInfo);

		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.PASSWORD, userInfo.getPassword());
		claim.put(JWTUtil.USER_NAME, userInfo.getUserName());
		return JWTUtil.generateToken(claim);
	}

	private List<String> asList(String role) {
		List<String> roles = new ArrayList<String>();
		roles.add(role);
		return roles;
	}

	@Override
	public String login(String username, String password) {
		// UsernamePasswordAuthenticationToken upToken = new
		// UsernamePasswordAuthenticationToken(username, password);
		// Authentication authentication =
		// authenticationManager.authenticate(upToken);
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.PASSWORD, userDetails.getPassword());
		claim.put(JWTUtil.USER_NAME, userDetails.getUsername());

		return JWTUtil.generateToken(claim);

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
