package com.zm.pay.bussiness.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.WeixinPayConfig;

public interface PayMapper {

	List<AliPayConfigModel> listAliPayConfig();
	
	List<WeixinPayConfig> listWeixinPayConfig();
	
	AliPayConfigModel getAliPayConfig(@Param("centerId") Integer centerId);
	
	WeixinPayConfig getWeixinPayConfig(@Param("centerId") Integer centerId);
}
