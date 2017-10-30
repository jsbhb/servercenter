package com.zm.goods;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
//@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableTransactionManagement
@EnableSwagger2
public class GoodsApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(GoodsApplication.class).web(true).run(args);
	}
}
