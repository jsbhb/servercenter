package com.zm.supplier.base;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.supplier.bussiness.dao.SupplierMapper;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.SupplierInterface;

@Component
public class SysInit {

	@Resource
	RedisTemplate<String, SupplierInterface> redisTemplate;

	@Resource
	SupplierMapper supplierMapper;
	
	@PostConstruct
	public void init(){
		
		loadSupplierInfConfig();
		
	}
	
	private void loadSupplierInfConfig(){
		List<SupplierInterface> list = supplierMapper.listSupplierInterface();
		if(list == null){
			return;
		}
		for(SupplierInterface model : list){
			redisTemplate.opsForValue().set(Constants.SUPPLIER_INTERFACE+model.getSupplierId(), model);
		}
	}
}
