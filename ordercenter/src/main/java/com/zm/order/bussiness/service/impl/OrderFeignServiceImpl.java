package com.zm.order.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.service.CacheAbstractService;
import com.zm.order.bussiness.service.OrderFeignService;
import com.zm.order.component.CacheComponent;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.bo.GradeBO;

@Service
public class OrderFeignServiceImpl implements OrderFeignService {

	@Resource
	CacheAbstractService cacheAbstractService;
	
	@Override
	public void syncGrade(GradeBO grade) {
		LogUtil.writeLog("开始同步grade===="+ CacheComponent.getInstance().getSet().toString());
		int startSize = CacheComponent.getInstance().getSet().size();
		//加入缓存
		CacheComponent.getInstance().addGrade(grade);
		int endSize = CacheComponent.getInstance().getSet().size();
		if(endSize > startSize){
			//初始化统计缓存
			cacheAbstractService.initNewGradeCache(grade.getId());
		}
		LogUtil.writeLog("同步grade结束===="+ CacheComponent.getInstance().getSet().toString());
	}

}
