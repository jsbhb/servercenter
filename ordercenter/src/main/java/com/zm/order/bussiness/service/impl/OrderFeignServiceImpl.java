package com.zm.order.bussiness.service.impl;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.service.OrderFeignService;
import com.zm.order.component.CacheComponent;
import com.zm.order.log.LogUtil;
import com.zm.order.pojo.bo.GradeBO;

@Service
public class OrderFeignServiceImpl implements OrderFeignService {

	
	@Override
	public void syncGrade(GradeBO grade) {
		LogUtil.writeLog("开始同步grade===="+ CacheComponent.getInstance().getSet().toString());
		//加入缓存
		CacheComponent.getInstance().addGrade(grade);
		
		LogUtil.writeLog("同步grade结束===="+ CacheComponent.getInstance().getSet().toString());
	}

}
