package com.zm.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 
 * ClassName: Application <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Jul 31, 2017 3:36:52 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthApplication {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(AuthApplication.class).web(true).run(args);
	}
}
