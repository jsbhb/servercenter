package com.zm.order;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 
 * ClassName: Application <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Jul 27, 2017 4:53:10 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OrderApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(OrderApplication.class).web(true).run(args);
	}
}
