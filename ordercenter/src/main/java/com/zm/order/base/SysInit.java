package com.zm.order.base;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zm.order.bussiness.service.CacheAbstractService;

@Component
public class SysInit {
	
	@Resource
	CacheAbstractService cacheAbstractService;
	
	@PostConstruct
	public void init(){
		cacheAbstractService.initCache();
	}
	
}
