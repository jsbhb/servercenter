/**  
 * Project Name:authserver  
 * File Name:UserController.java  
 * Package Name:com.zm.gateway.controller  
 * Date:Aug 8, 201711:44:44 PM  
 * Copyright (c) 2017, 306494983@qq.com All Rights Reserved.  
 *  
*/

package com.zm.auth.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.auth.common.Constants;
import com.zm.auth.feignclient.ThirdPartFeignClient;
import com.zm.auth.model.ErrorCodeEnum;
import com.zm.auth.model.GetTokenParam;
import com.zm.auth.model.ResultPojo;
import com.zm.auth.model.UserInfo;
import com.zm.auth.service.AuthService;
import com.zm.auth.util.JSONUtil;

/**
 * ClassName: UserController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 8, 2017 11:44:44 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@RestController
@RequestMapping(value = "/auth")
public class UserController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthService authService;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultPojo createAuthenticationToken(@RequestBody UserInfo userInfo) throws AuthenticationException {
		try {
			return new ResultPojo(authService.login(userInfo));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request)
			throws AuthenticationException {
		String token = request.getHeader(tokenHeader);
		String refreshedToken = authService.refresh(token);
		if (refreshedToken == null) {
			return ResponseEntity.badRequest().body(null);
		} else {
			return ResponseEntity.ok(refreshedToken);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResultPojo register(@RequestBody UserInfo userInfo) throws AuthenticationException {
		try {
			return new ResultPojo(authService.register(userInfo));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public ResultPojo check(@RequestBody UserInfo userInfo) throws AuthenticationException {
		try {
			return new ResultPojo(authService.checkAccount(userInfo));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	public ResultPojo modifyPwd(@RequestBody UserInfo userInfo, @RequestParam("code") String code)
			throws AuthenticationException {
		try {
			boolean flag = thirdPartFeignClient.verifyPhoneCode(Constants.FIRST_VERSION, userInfo.getUserName(), code);
			if (flag) {
				return new ResultPojo(authService.modifyPwd(userInfo));
			} else {
				return new ResultPojo("1", "验证码错误");
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/platform/register", method = RequestMethod.POST)
	public ResultPojo createAccount(@RequestParam("userId") Integer userId,
			@RequestParam("platUserType") Integer platUserType) throws AuthenticationException {
		try {
			return new ResultPojo(authService.createAccount(userId, platUserType));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/get_token", method = RequestMethod.POST)
	public ResultPojo getToken(HttpServletRequest req) {
		String data = req.getParameter("data");
		GetTokenParam param = null;
		try {
			param = JSONUtil.parse(data, GetTokenParam.class);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return new ResultPojo(ErrorCodeEnum.FORMAT_ERROR.getErrorCode(), ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}
		if (Constants.FIRST_VERSION.equals(param.getVersion())) {
			try {
				return authService.getToken(param);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultPojo(ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
		}
		return new ResultPojo(ErrorCodeEnum.VERSION_ERROR.getErrorCode(), ErrorCodeEnum.VERSION_ERROR.getErrorMsg());

	}

}
