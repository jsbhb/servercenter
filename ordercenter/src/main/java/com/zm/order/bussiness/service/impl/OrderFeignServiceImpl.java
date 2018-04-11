package com.zm.order.bussiness.service.impl;

import org.springframework.stereotype.Service;

import com.zm.order.bussiness.service.OrderFeignService;
import com.zm.order.component.CacheComponent;
import com.zm.order.pojo.bo.GradeBO;

@Service
public class OrderFeignServiceImpl implements OrderFeignService {

	
	@Override
	public void syncGrade(GradeBO grade) {
		//加入缓存
		CacheComponent.getInstance().addGrade(grade);
	}

}
