package com.zm.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * ClassName: AuthenticationController <br/>
 * Function: 认证服务器. <br/>
 * date: Aug 24, 2017 1:31:45 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@RestController
public class AuthenticationController {
	
	@RequestMapping(value = "/authentication", method = RequestMethod.GET)
	public ResponseEntity<?> authentication() throws AuthenticationException {
		return ResponseEntity.ok("认证通过！");
	}
}
