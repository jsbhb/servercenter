package com.zm.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.auth.common.Constants;
import com.zm.auth.common.JWTUtil;
import com.zm.auth.common.MethodUtil;
import com.zm.auth.feignclient.UserCenterFeignClient;
import com.zm.auth.mapper.UserMapper;
import com.zm.auth.model.AccessToken;
import com.zm.auth.model.ErrorCodeEnum;
import com.zm.auth.model.GetTokenParam;
import com.zm.auth.model.PlatUserType;
import com.zm.auth.model.PlatformUser;
import com.zm.auth.model.ResultPojo;
import com.zm.auth.model.SecurityUserDetail;
import com.zm.auth.model.SecurityUserFactory;
import com.zm.auth.model.UserInfo;
import com.zm.auth.service.AuthService;
import com.zm.auth.util.SignUtil;

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

	@Resource
	UserCenterFeignClient userCenterFeignClient;

	public static final Integer B2B_FORM = 6;

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

		if (userInfo.getUserCenterId() == 0) {
			throw new SecurityException("没用用户中心编号！");
		}

		int loginType = userInfo.getLoginType();

		String userName = null;
		if (loginType == Constants.LOGIN_PHONE) {
			userName = userInfo.getPhone();
			userInfo.setUserName(userName);
			if (userMapper.getUserByPhone(userInfo) != null) {
				throw new SecurityException("该手机号已经存在！");
			}
		}

		if (loginType == Constants.LOGIN_USER_NAME) {
			userName = userInfo.getUserName();
			if (userMapper.getUserByName(userInfo) != null) {
				throw new SecurityException("该用户名名已经存在！");
			}
		}

		if (loginType != Constants.LOGIN_PLATID) {
			// 设置密码MD5加密
			userInfo.setPassword(MethodUtil.MD5(userInfo.getPassword()));
			if (userName == null || "".equals(userName)) {
				throw new SecurityException("用户名信息有误，请重新输入！");
			}

		}

		userInfo.setLastPasswordResetDate(new Date());
		userInfo.setAuthorities(asList("ROLE_USER"));
		Set<PlatformUser> set = new HashSet<PlatformUser>();
		String userId = null;
		if(PlatUserType.CROSS_BORDER.getIndex() == userInfo.getPlatUserType()){
			userMapper.insert(userInfo);
			userId = userInfo.getUserId();
		}else{
			userId = userMapper.getUserIdByUserName(userName);
			if(userId == null){
				userMapper.insert(userInfo);
				userId = userInfo.getUserId();
			}
			//插入平台用户
			PlatformUser consumerPlat = new PlatformUser(userId,PlatUserType.CONSUMER.getIndex());
			PlatformUser pushUserPlat = new PlatformUser(userId,PlatUserType.PUSH_USER.getIndex());
			set.add(pushUserPlat);
			set.add(consumerPlat);
			//end
		}
		PlatformUser platformUser = new PlatformUser(userId,userInfo.getPlatUserType());
		set.add(platformUser);
		userMapper.insertPlatformUser(set);

		SecurityUserDetail userDetail = SecurityUserFactory.createWithOutPassWord(userInfo);

		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.PASSWORD, userInfo.getPassword());
		claim.put(JWTUtil.USER_NAME, userInfo.getUserName());
		claim.put(JWTUtil.PLATUSERTYPE, userInfo.getPlatUserType());
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

		int platUserType = userInfo.getPlatUserType();

		UserInfo userDetail = null;
		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.PLATUSERTYPE, platUserType);
		if (loginType == Constants.LOGIN_WX) {
			if (userInfo.getOpenId() == null || "".equals(userInfo.getOpenId())) {
				throw new SecurityException("未传递openId给后台！");
			}

			userDetail = userMapper.queryByOpenId(userInfo);

			if (userDetail == null) {
				String userId = userMapper.getUserIdByOpenId(userInfo.getOpenId());
				if(userId == null){
					userMapper.insert(userInfo);
					userId = userInfo.getUserId();
				}
				PlatformUser platformUser = new PlatformUser(userInfo.getUserId(),userInfo.getPlatUserType());
				Set<PlatformUser> set = new HashSet<PlatformUser>();
				set.add(platformUser);
				userMapper.insertPlatformUser(set);
				userDetail = userInfo;
				userInfo = null;
			}

			claim.put(JWTUtil.OPEN_ID, userDetail.getOpenId());

		} else {
			if (loginType == Constants.LOGIN_PHONE) {
				claim.put(JWTUtil.USER_NAME, userInfo.getPhone());
				userInfo.setUserName(userInfo.getPhone());
			}

			if (loginType == Constants.LOGIN_USER_NAME)
				claim.put(JWTUtil.USER_NAME, userInfo.getUserName());

			if (loginType == Constants.LOGIN_PLATID) {
				claim.put(JWTUtil.PLATFORM_ID, userInfo.getUserId());
				userDetail = userMapper.getUserByPlatId(userInfo);
			}

			if (platUserType == PlatUserType.CONSUMER.getIndex()
					|| platUserType == PlatUserType.B2B_BUSSINESS.getIndex()
					|| platUserType == PlatUserType.PUSH_USER.getIndex()) {

				claim.put(JWTUtil.PASSWORD, MethodUtil.MD5(userInfo.getPassword()));
				claim.put(JWTUtil.USER_NAME, userInfo.getUserName());

				userInfo.setPassword(MethodUtil.MD5(userInfo.getPassword()));
				userDetail = userMapper.getUserForLogin(userInfo);
			}
		}

		if (userDetail == null) {
			throw new SecurityException("登录失败，用户名或密码错误，请重新输入！");
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

		if (loginType == Constants.LOGIN_PHONE) {
			userInfo.setUserName(userInfo.getPhone());
		}

		if (userInfo.getUserName() == null || "".equals(userInfo.getUserName())) {
			throw new SecurityException("用户名信息有误，请重新输入！");
		}

		if (userMapper.getUserByName(userInfo) != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean modifyPwd(UserInfo userInfo) {
		userInfo.setPassword(MethodUtil.MD5(userInfo.getPassword()));
		int num = userMapper.modifyPwd(userInfo);
		if (num == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean createAccount(Integer userId, Integer platUserType) {
		String phone = userCenterFeignClient.getPhoneByUserId(Constants.FIRST_VERSION, userId);
		if (phone == null) {
			return false;
		}

		UserInfo tempUser = new UserInfo();
		tempUser.setUserName(phone);
		tempUser.setPlatUserType(platUserType);
		UserInfo userInfo = userMapper.getUserByName(tempUser);
		if (userInfo == null) {
			String authUserId = userMapper.getUserIdByUserName(phone);
			PlatformUser consumerPlat = null;
			PlatformUser pushUserPlat = null;
			PlatformUser platformUser = null;
			Set<PlatformUser> set = new HashSet<PlatformUser>();
			if(authUserId == null){
				userInfo = new UserInfo();
				userInfo.setUserName(phone);
				userInfo.setUserCenterId(userId);
				userInfo.setPlatUserType(platUserType);
				userInfo.setPassword(MethodUtil.MD5("000000"));// 密码默认6个0
				userInfo.setPhone(phone);
				userMapper.insert(userInfo);
				authUserId = userInfo.getUserId();
				consumerPlat = new PlatformUser(authUserId,PlatUserType.CONSUMER.getIndex());
				pushUserPlat = new PlatformUser(authUserId,PlatUserType.PUSH_USER.getIndex());
				set.add(pushUserPlat);
				set.add(consumerPlat);
			}
			platformUser = new PlatformUser(authUserId,platUserType);
			set.add(platformUser);
			userMapper.insertPlatformUser(set);
		}

		return true;
	}

	@Override
	public ResultPojo getToken(GetTokenParam param) {
		if (!param.validata()) {
			return new ResultPojo(ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		UserInfo temUser = new UserInfo();
		temUser.setUserName(param.getAppKey());
		temUser.setPlatUserType(PlatUserType.BUTTJOINT.getIndex());
		UserInfo info = userMapper.getUserByName(temUser);
		String appSecret = info.getPassword();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appSecret", appSecret);
		map.put("version", param.getVersion());
		map.put("appKey", param.getAppKey());
		map.put("nonceStr", param.getNonceStr());
		String sing = SignUtil.sign(map);
		if (!param.getSign().equals(sing)) {
			return new ResultPojo(ErrorCodeEnum.SIGN_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.SIGN_VALIDATE_ERROR.getErrorMsg());
		}
		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(JWTUtil.APPKEY, param.getAppKey());
		claim.put("expires", JWTUtil.EXPIRES);
		claim.put(JWTUtil.PLATUSERTYPE, PlatUserType.BUTTJOINT.getIndex());
		String token = JWTUtil.generateLimitTimeToken(claim);
		return new ResultPojo(new AccessToken(token, JWTUtil.EXPIRES));
	}

}
