package com.zm.supplier.bussiness.dao;

import java.util.List;

import com.zm.supplier.pojo.Express;

public interface SupplierMapper {

	List<Express> listExpressBySupplierId(Integer supplierId);
}
