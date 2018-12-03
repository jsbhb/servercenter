package com.zm.goods.activity.front.dao;

import java.util.List;
import java.util.Map;

import com.zm.goods.activity.model.bargain.po.UserBargainPO;

public interface BargainMapper {

	List<UserBargainPO> listBargainByUserId(Integer userId);
	
	UserBargainPO getBargainDetailByParam(Map<String,Object> param);
}
