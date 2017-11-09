package com.zm.thirdcenter.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.thirdcenter.bussiness.dao.LoginPluginMapper;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.WXLoginConfig;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Resource
	LoginPluginMapper loginPluginMapper;
	
	@PostConstruct
	public void init(){
		
		loadWXLoginConfig();
		
	}
	
	private void loadWXLoginConfig(){
		List<WXLoginConfig> list = loginPluginMapper.listWXLoginConfig();
		for(WXLoginConfig model : list){
			redisTemplate.opsForValue().set(Constants.LOGIN+model.getCenterId()+""+model.getLoginType(), model);
		}
	}
}
