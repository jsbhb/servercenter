package com.zm.user.bussiness.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.UserService;
import com.zm.user.pojo.Address;
import com.zm.user.pojo.ResultPojo;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.utils.RegularUtil;
import com.zm.user.wx.ApiResult;

@Service
public class UserServiceImpl implements UserService{

	private static final Integer DEFAULT = 1;
	
	@Resource
	UserMapper userMapper;
	
	@Override
	public boolean userNameVerify(Map<String, String> param) {

		int count = userMapper.userNameVerify(param);
		if(count > 0){
			return false;
		}
		return true;
	}

	@Override
	public void saveAddress(Address address) {
		
		if(DEFAULT.equals(address.getSetDefault())){
			userMapper.updateUndefaultAddress(address.getUserId());
		}
		
		userMapper.saveAddress(address);
	}

	@Override
	public ResultPojo updateAddress(Map<String, String> param) {
		
		ResultPojo result = new ResultPojo();
		
		if(DEFAULT.equals(param.get("setDefault"))){
			try {
				userMapper.updateUndefaultAddress(Integer.valueOf(param.get("userId")));
			} catch (NumberFormatException e) {
				result.setSuccess(false);
				result.setErrorMsg("userId参数有误");
			}
		}
		userMapper.updateAddress(param);
		result.setSuccess(true);
		return result;
	}

	@Override
	public void updateUserDetail(Map<String, String> param) {
		
		userMapper.updateUserDetail(param);
	}

	@Override
	public void saveUser(UserInfo info) {
		
		userMapper.saveUser(info);
		
		if(info.getUserDetail() != null){
			userMapper.saveUserDetail(info.getUserDetail());
		}
	}

	@Override
	public void removeAddress(Map<String, Object> param) {
		
		userMapper.removeAddress(param);
		
	}

	@Override
	public boolean verifyWechatIsRegister(String unionid) {
		
		int count = userMapper.queryByWechatUnionid(unionid);
		
		if(count > 0){
			return true;
		}
		
		return false;
	}

	@Override
	public UserInfo packageUser(ApiResult apiResult) {

		UserInfo info = new UserInfo();
		info.setWechat(apiResult.get("unionid")+"");
		UserDetail userDetail = new UserDetail();
		boolean isNum = RegularUtil.isNumeric(apiResult.get("sex")+"");
		if(isNum){
			userDetail.setSex(Integer.valueOf(apiResult.get("sex")+""));
		} else {
			userDetail.setSex(0);
		}
		
		userDetail.setLocation(apiResult.get("country")+" "+apiResult.get("province")+" "+apiResult.get("city"));
		userDetail.setHeadImg(apiResult.get("headimgurl")+"");
		userDetail.setNickName(apiResult.get("nickname")+"");
		
		info.setUserDetail(userDetail);
		return info;
	}

	@Override
	public void saveUserDetail(UserDetail info) {
		
		userMapper.saveUserDetail(info);
	}

	@Override
	public void modifyPwd(Map<String, Object> param) {
		
		userMapper.updateUserPwd(param);
		
	}

}
