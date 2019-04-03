package com.zm.supplier.bussiness.controller;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.supplier.bussiness.component.CustomsAddSignatureComponent;

@RestController
public class SupplierFeignController {

	@Resource
	CustomsAddSignatureComponent customsAddSignatureComponent;
	
	
	@RequestMapping(value = "supplier/zs/sign", method = RequestMethod.POST)
	public String getSign(@RequestBody String originData){
		try {
			return customsAddSignatureComponent.signature(originData);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
