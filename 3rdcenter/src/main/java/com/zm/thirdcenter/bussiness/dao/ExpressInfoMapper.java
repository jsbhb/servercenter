package com.zm.thirdcenter.bussiness.dao;

import java.util.List;

import com.zm.thirdcenter.pojo.ExpressInterface;

public interface ExpressInfoMapper {
	
	ExpressInterface getExpressInterface(String expressCode);
	
	List<ExpressInterface> getExpressList();
}
