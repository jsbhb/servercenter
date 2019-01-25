package com.zm.thirdcenter.bussiness.express.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.thirdcenter.bussiness.component.ExpressInfoThreadPool;
import com.zm.thirdcenter.bussiness.express.service.ExpressInfoService;
import com.zm.thirdcenter.pojo.OrderInfo;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ExpressInfoServiceImpl implements ExpressInfoService {

	@Resource
	ExpressInfoThreadPool expressInfoThreadPool;

	@Override
	public Map<String, Object> createExpressInfoByExpressCode(List<OrderInfo> infoList, String expressCode) {
		return expressInfoThreadPool.createExpressInfo(infoList, expressCode);
	}
}
