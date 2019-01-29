package com.zm.thirdcenter.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.thirdcenter.pojo.WXLoginConfig;

public interface LoginPluginMapper {

	List<WXLoginConfig> listWXLoginConfig();
	
	WXLoginConfig getWXLoginConfig(Map<String,Object> temMap);
}
