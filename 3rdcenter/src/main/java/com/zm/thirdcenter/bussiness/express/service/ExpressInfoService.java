package com.zm.thirdcenter.bussiness.express.service;

import java.util.List;
import java.util.Map;

import com.zm.thirdcenter.pojo.OrderInfo;

public interface ExpressInfoService {

	Map<String, Object> createExpressInfoByExpressCode(List<OrderInfo> infoList, String expressCode);
}
