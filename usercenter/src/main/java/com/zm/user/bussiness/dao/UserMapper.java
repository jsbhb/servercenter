package com.zm.user.bussiness.dao;

import java.util.Map;

import com.zm.user.pojo.Address;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;

public interface UserMapper {

	int userNameVerify(Map<String,String> param);
	
	void saveAddress(Address address);
	
	void updateUndefaultAddress(Integer userId);
	
	void updateAddress(Map<String,String> param);
	
	void updateUserDetail(Map<String,String> param);
	
	void saveUserDetail(UserDetail userDetail);
	
	void saveUser(UserInfo info);
	
	void removeAddress(Map<String,Object> param);
	
	int queryByWechatUnionid(String openId);
	
	void updateUserPwd(Map<String,Object> param);
}
