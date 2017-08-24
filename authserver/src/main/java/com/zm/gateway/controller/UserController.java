/**  
 * Project Name:authserver  
 * File Name:UserController.java  
 * Package Name:com.zm.gateway.controller  
 * Date:Aug 8, 201711:44:44 PM  
 * Copyright (c) 2017, 306494983@qq.com All Rights Reserved.  
 *  
*/

package com.zm.gateway.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.gateway.model.UserInfo;
import com.zm.gateway.service.AuthService;

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

	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserInfo userInfo) throws AuthenticationException {
		final String token = authService.login(userInfo.getUserName(), userInfo.getPassword());
		return ResponseEntity.ok(token);
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
	public ResponseEntity<?> register(@RequestBody UserInfo userInfo) throws AuthenticationException {
		return ResponseEntity.ok(authService.register(userInfo));
	}

}
