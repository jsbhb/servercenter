package com.zm.supplier.bussiness.service;

import com.zm.supplier.common.ResultModel;
import com.zm.supplier.pojo.callback.OrderStatusCallBack;

public interface SupplierCallbackService {

	ResultModel orderStatusCallBack(OrderStatusCallBack statusCallBack);
}
