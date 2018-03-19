package com.zm.pay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration  
@EnableAutoConfiguration 
@ConfigurationProperties(prefix="spring.redis")
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String hostName;
	
	@Value("${spring.redis.port}")
	private String port;
	
	@Value("${spring.redis.password}")
	private String password;
	
	Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Bean
	public JedisPoolConfig getRedisConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(200);
		config.setMaxTotal(300);
		config.setTestOnBorrow(false);
		config.setTestOnReturn(false);
		return config;
	}

	@Bean
	public JedisConnectionFactory getConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		JedisPoolConfig config = getRedisConfig();
		logger.info("hostName:"+hostName+",port:"+port+",password:"+password);
		factory.setHostName(hostName);
		factory.setPort(Integer.valueOf(port));
		factory.setPassword(password);
		factory.setPoolConfig(config);
		return factory;
	}

	@Bean
	public RedisTemplate<?, ?> getRedisTemplate() {
		RedisTemplate<?, ?> template = new StringRedisTemplate(getConnectionFactory());
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<Object> jdkSerializer = new JdkSerializationRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(jdkSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(stringSerializer);
		return template;
	}
}
