package com.zm.supplier.bussiness.service;

import java.util.List;
import java.util.Map;

import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.OrderInfo;

public interface SupplierService {

	List<Express> listExpressBySupplierId(Integer supplierId);

	Map<String, Object> sendOrder(OrderInfo info);
}
