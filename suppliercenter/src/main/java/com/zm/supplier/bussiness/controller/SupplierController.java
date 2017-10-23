package com.zm.supplier.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.supplier.bussiness.service.SupplierService;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.ResultModel;

@RestController
public class SupplierController {
	
	@Resource
	SupplierService supplierService;

	@RequestMapping(value = "{version}/supplier/express/{supplierId}", method = RequestMethod.GET)
	public ResultModel listExpressBySupplierId(@PathVariable("version") Double version,
			@PathVariable("supplierId") Integer supplierId) {

		if(Constants.FIRST_VERSION.equals(version)){
			return new ResultModel(true, supplierService.listExpressBySupplierId(supplierId));
		}
		
		return new ResultModel(false, "版本错误");
	}
}
