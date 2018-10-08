package com.config.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

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
//@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ConfigServerApplication.class).web(true).run(args);
	}
}
