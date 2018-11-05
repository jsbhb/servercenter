package com.zm.pay.bussiness.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.UnionPayConfig;
import com.zm.pay.pojo.WeixinPayConfig;
import com.zm.pay.pojo.YopConfigModel;

public interface PayMapper {

	List<AliPayConfigModel> listAliPayConfig();
	
	List<WeixinPayConfig> listWeixinPayConfig();
	
	AliPayConfigModel getAliPayConfig(@Param("centerId") Integer centerId);
	
	WeixinPayConfig getWeixinPayConfig(@Param("centerId") Integer centerId);
	
	List<UnionPayConfig> listUnionPayConfig();
	
	UnionPayConfig getUnionPayConfig(@Param("centerId") Integer centerId);
	
	List<YopConfigModel> listYopPayConfig();

	YopConfigModel getYopPayConfig(@Param("centerId") Integer clientId);
}
