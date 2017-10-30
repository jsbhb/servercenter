package com.zm.pay;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import feign.Retryer;
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
public class PayApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(PayApplication.class).web(true).run(args);
	}

	//feign不进行重试
	@Bean
	Retryer feignRetryer() {
		return Retryer.NEVER_RETRY;
	}
}
