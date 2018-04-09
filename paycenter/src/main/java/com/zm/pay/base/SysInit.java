package com.zm.pay.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.pay.bussiness.dao.PayMapper;
import com.zm.pay.constants.Constants;
import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.UnionPayConfig;
import com.zm.pay.pojo.WeixinPayConfig;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	PayMapper payMapper;
	
	@PostConstruct
	public void init(){
		
		loadAliPayConfig();
		
		loadWeixinPayConfig();
		
		loadUnionPayConfig();
	}
	
	private void loadAliPayConfig(){
		List<AliPayConfigModel> list = payMapper.listAliPayConfig();
		for(AliPayConfigModel model : list){
			template.opsForValue().set(Constants.PAY+model.getCenterId()+Constants.ALI_PAY, model);
		}
	}
	
	private void loadWeixinPayConfig(){
		List<WeixinPayConfig> list = payMapper.listWeixinPayConfig();
		for(WeixinPayConfig model : list){
			template.opsForValue().set(Constants.PAY+model.getCenterId()+Constants.WX_PAY, model);
		}
	}
	
	private void loadUnionPayConfig(){
		List<UnionPayConfig> list = payMapper.listUnionPayConfig();
		for(UnionPayConfig model : list){
			template.opsForValue().set(Constants.PAY+model.getCenterId()+Constants.UNION_PAY, model);
			template.opsForValue().set(Constants.PAY+model.getCenterId()+Constants.UNION_PAY_MER_ID, model.getMerId());
		}
	}
}
