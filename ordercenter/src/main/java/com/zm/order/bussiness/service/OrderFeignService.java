package com.zm.order.bussiness.service;

import com.zm.order.pojo.bo.CustomOrderReturn;
import com.zm.order.pojo.bo.GradeBO;

public interface OrderFeignService {

	void syncGrade(GradeBO grade);

	void saveCustomOrderExpress(CustomOrderReturn orderExpress);

	String listOrderGoodsInfo(String orderId);

	String getOrderDetail(String orderId);
	
	String getOrderExpressDetail(String orderId);
}
