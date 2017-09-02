package com.zm.pay.bussiness.dao;

import java.util.List;

import com.zm.pay.pojo.AliPayConfigModel;
import com.zm.pay.pojo.WeixinPayConfig;

public interface PayMapper {

	List<AliPayConfigModel> listAliPayConfig();
	
	List<WeixinPayConfig> listWeixinPayConfig();
}
