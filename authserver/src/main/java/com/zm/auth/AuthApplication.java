package com.zm.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
