package com.zm.order.bussiness.service;

import com.zm.order.pojo.ButtJointOrder;
import com.zm.order.pojo.ResultModel;

public interface OrderOpenInterfaceService {

	ResultModel addOrder(ButtJointOrder orderInfo);

	ResultModel getOrderStatus(String orderId);
}
