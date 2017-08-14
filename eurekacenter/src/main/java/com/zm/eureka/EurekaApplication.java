package com.zm.eureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

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
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(EurekaApplication.class).web(true).run(args);
	}
}
