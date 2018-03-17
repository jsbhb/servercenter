package com.zm.finance.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.finance.bussiness.dao.CapitalPoolMapper;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.capitalpool.CapitalPool;
import com.zm.finance.util.JSONUtil;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, Object> template;

	@Resource
	CapitalPoolMapper capitalPoolMapper;

	@PostConstruct
	public void init() {
	
		loadCapitalPool();
	}

	/**
	 * @fun 加载资金池到redis，如果redis存在则不加载
	 */
	@SuppressWarnings("unchecked")
	private void loadCapitalPool() {
		List<CapitalPool> poolList = capitalPoolMapper.listCenterCapitalPool();
		if (poolList != null && poolList.size() > 0) {
			HashOperations<String, Object, Object> hashOperations = template.opsForHash();
			String key;
			Map<String, Object> temp = null;
			Map<String, String> result = new HashMap<String, String>();
			for (CapitalPool pool : poolList) {
				key = Constants.CAPITAL_PERFIX + pool.getCenterId();
				if (!hashOperations.hasKey(key, key)) {
					temp = JSONUtil.parse(JSONUtil.toJson(pool), Map.class);
					for(Entry<String, Object> entry : temp.entrySet()){
						if(entry.getValue() != null){
							result.put(entry.getKey(), entry.getValue().toString());
						}
					}
					hashOperations.putAll(key, result);
				}
			}
		}
	}

}
