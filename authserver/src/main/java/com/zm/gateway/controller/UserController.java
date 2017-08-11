/**  
 * Project Name:authserver  
 * File Name:UserController.java  
 * Package Name:com.zm.gateway.controller  
 * Date:Aug 8, 201711:44:44 PM  
 * Copyright (c) 2017, 306494983@qq.com All Rights Reserved.  
 *  
*/

package com.zm.gateway.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UserController {
	@GetMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}
