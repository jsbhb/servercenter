package com.zm.order.bussiness.service;

import com.zm.order.pojo.ResultModel;

public interface OrderOpenInterfaceService {

	ResultModel addOrder(String orderInfo) throws Exception;

	ResultModel getOrderStatus(String json);

	ResultModel payCustom(String data);
}
