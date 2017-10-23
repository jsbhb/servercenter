package com.zm.supplier.bussiness.service;

import java.util.List;

import com.zm.supplier.pojo.Express;

public interface SupplierService {

	List<Express> listExpressBySupplierId(Integer supplierId);
}
