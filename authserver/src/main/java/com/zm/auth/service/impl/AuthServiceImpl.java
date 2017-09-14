package com.zm.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.auth.common.Constants;
import com.zm.auth.common.JWTUtil;
import com.zm.auth.common.MethodUtil;
import com.zm.auth.mapper.UserMapper;
import com.zm.auth.model.PlatUserType;
import com.zm.auth.model.SecurityUserDetail;
import com.zm.auth.model.SecurityUserFactory;
import com.zm.auth.model.UserInfo;
import com.zm.auth.service.AuthService;

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

		if (userInfo == null) {
			throw new SecurityException("用户信息未输入！");
		}
		
		if(userInfo.getUserCenterId() == 0){
			throw new SecurityException("没用用户中心编号！");
		}

		int loginType = userInfo.getLoginType();

		String userName = null;
		if (loginType == Constants.LOGIN_PHONE) {
			userName = userInfo.getPhone();
			if (userMapper.getUserByPhone(userName) != null) {
				throw new SecurityException("该手机号已经存在！");
			}
		}

		if (loginType == Constants.LOGIN_USER_NAME){
			userName = userInfo.getUserName();
			if (userMapper.getUserByName(userName) != null) {
				throw new SecurityException("该用户名名已经存在！");
			}			
		}

		if (userName == null || "".equals(userName)) {
			throw new SecurityException("用户名信息有误，请重新输入！");
		}

		

		if (loginType == Constants.LOGIN_PHONE) {
			userInfo.setUserName(userName);
		}

		// 设置密码MD5加密
		userInfo.setPassword(MethodUtil.MD5(userInfo.getPassword()));
		userInfo.setLastPasswordResetDate(new Date());
		userInfo.setAuthorities(asList("ROLE_USER"));
		userMapper.insert(userInfo);

		SecurityUserDetail userDetail = SecurityUserFactory.createWithOutPassWord(userInfo);

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

		if (userInfo == null) {
			throw new SecurityException("用户信息未输入！");
		}

		int loginType = userInfo.getLoginType();

		UserInfo userDetail = null;
		if (loginType == Constants.LOGIN_WX) {
			if (userInfo.getOpenId() == null || "".equals(userInfo.getOpenId())) {
				throw new SecurityException("未传递openId给后台！");
			}

			userDetail = userMapper.queryByOpenId(userInfo);

			if (userDetail == null) {
				userMapper.insert(userInfo);
				userDetail = userInfo;
				userInfo = null;
			}
			

		} else {
			String userName = null;
			if (loginType == Constants.LOGIN_PHONE) {
				userName = userInfo.getPhone();
				userInfo.setUserName(userInfo.getPhone());
			}

			if (loginType == Constants.LOGIN_USER_NAME)
				userName = userInfo.getUserName();

			if (userName == null || "".equals(userName)) {
				throw new SecurityException("用户名信息有误，请重新输入！");
			}

			int platUserType = userInfo.getPlatUserType();

			if (platUserType == PlatUserType.CONSUMER.getIndex()) {
				String pwd = userInfo.getPassword();
				userInfo.setPassword(MethodUtil.MD5(pwd));
				userDetail = userMapper.getUserForLogin(userInfo);
			} else if (platUserType >= 1 && platUserType < 5) {
				userDetail = userMapper.getUserByPlatId(userInfo.getUserId());
			}
		}

		if (userDetail == null) {
			throw new SecurityException("登录失败，没有该用户！");
		}

		Map<String, Object> claim = new HashMap<String, Object>();
		if (loginType == Constants.LOGIN_WX) {
			claim.put(JWTUtil.OPEN_ID, userDetail.getOpenId());
		} else {
			claim.put(JWTUtil.PASSWORD, userDetail.getPassword());
			claim.put(JWTUtil.USER_NAME, userDetail.getUserName());
		}
		userDetail.setAuthorities(asList("ROLE_USER"));

		SecurityUserDetail securityUserDetail = SecurityUserFactory.createWithOutPassWord(userDetail);

		securityUserDetail.setToken(JWTUtil.generateToken(claim));

		return securityUserDetail;
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

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.zm.auth.service.AuthService#checkAccount(com.zm.auth.model.UserInfo)
	 */
	@Override
	public boolean checkAccount(UserInfo userInfo) {

		if (userInfo == null) {
			throw new SecurityException("用户信息未输入！");
		}

		int loginType = userInfo.getLoginType();

		String userName = null;
		if (loginType == Constants.LOGIN_PHONE) {
			userName = userInfo.getPhone();
		}

		if (loginType == Constants.LOGIN_USER_NAME)
			userName = userInfo.getUserName();

		if (userName == null || "".equals(userName)) {
			throw new SecurityException("用户名信息有误，请重新输入！");
		}

		if (userMapper.getUserByName(userName) != null) {
			return true;
		}

		return false;
	}

}
