package com.zm.supplier.bussiness.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.bussiness.service.SupplierService;
import com.zm.supplier.pojo.Express;

@Service
public class SupplierServiceImpl implements SupplierService {

	@Resource
	SupplierMapper supplierMapper;
	
	@Override
	public List<Express> listExpressBySupplierId(Integer supplierId) {
		return supplierMapper.listExpressBySupplierId(supplierId);
	}

}
