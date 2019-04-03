package com.zm.supplier.bussiness.service;

import java.util.Map;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.pojo.callback.OrderStatusCallBack;

public interface SupplierCallbackService {

	ResultModel orderStatusCallBack(OrderStatusCallBack statusCallBack);

	String dolphinCallBack(Map<String, String> getParam, Map<String, String> postParam);

	String hzCustomsCallback(String content, String msgType, String dataDigest);

}
