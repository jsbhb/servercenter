package com.zm.pay.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.pay.bussiness.dao.PayMapper;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.WeixinPayConfig;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Resource
	PayMapper payMapper;
	
	@PostConstruct
	public void init(){
		
		loadAliPayConfig();
		
		loadWeixinPayConfig();
	}
	
	private void loadAliPayConfig(){
		List<AliPayConfigModel> list = payMapper.listAliPayConfig();
		for(AliPayConfigModel model : list){
			redisTemplate.opsForValue().set(model.getCenterId()+Constants.ALI_PAY, model);
		}
	}
	
	private void loadWeixinPayConfig(){
		List<WeixinPayConfig> list = payMapper.listWeixinPayConfig();
		for(WeixinPayConfig model : list){
			redisTemplate.opsForValue().set(model.getCenterId()+Constants.WX_PAY, model);
		}
	}
}
