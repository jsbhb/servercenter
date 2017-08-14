/**  
* Project Name:authserver  
* File Name:ResourceServerConfig.java  
* Package Name:com.zm.gateway.auth  
* Date:Aug 8, 201711:36:57 PM  
*  
*/

package com.zm.gateway.auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * ClassName: ResourceServerConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 8, 2017 11:36:57 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Override
	public void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().exceptionHandling()
//				.authenticationEntryPoint(
//						(request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//				.and().authorizeRequests().anyRequest().authenticated().and().httpBasic();
	}
}
