package com.zm.goods.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import com.zm.goods.bussiness.dao.GoodsItemMapper;
import com.zm.goods.constants.Constants;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> template;
	
	@Resource
	GoodsItemMapper goodsItemMapper;
	
	@PostConstruct
	public void init(){
		
//		initGoodsCache();
	}
	
	private void initGoodsCache(){
		List<String> list = goodsItemMapper.queryItemCodeAndConversion();
		SetOperations<String, Object> setOperations = template.opsForSet();
		template.delete(Constants.GOODS_CACHE);
		for(String str : list){
			setOperations.add(Constants.GOODS_CACHE, str);
		}
	}
	
}
