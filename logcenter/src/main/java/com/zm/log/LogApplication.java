package com.zm.log;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

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
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class LogApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(LogApplication.class).web(true).run(args);
	}
}
