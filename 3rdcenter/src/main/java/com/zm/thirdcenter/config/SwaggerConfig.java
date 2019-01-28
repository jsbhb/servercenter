package com.zm.thirdcenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.zm.thirdcenter";
    public static final String VERSION = "1.0.0";
    
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))//api接口包扫描路径
                .paths(PathSelectors.any())//可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("第三方内部接口文档")//设置文档的标题
            .description("第三方内部服务文档")//设置文档的描述->1.Overview
            .version(VERSION)//设置文档的版本信息-> 1.1 Version information
            .contact(new Contact("", "", ""))//设置文档的联系方式->1.2 Contact information
            .termsOfServiceUrl("")//设置文档的License信息->1.3 License information
            .build();
    }
}
