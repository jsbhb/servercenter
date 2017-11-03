package com.zm.supplier.bussiness.dao;

import java.util.List;

import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.SupplierInterface;

public interface SupplierMapper {

	List<Express> listExpressBySupplierId(Integer supplierId);
	
	List<SupplierInterface> listSupplierInterface();
}
