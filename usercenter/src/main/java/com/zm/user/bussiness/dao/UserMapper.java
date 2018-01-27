package com.zm.user.bussiness.dao;

import java.util.List;
import java.util.Map;

import com.zm.user.pojo.Address;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ThirdLogin;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.UserVip;
import com.zm.user.pojo.VipOrder;
import com.zm.user.pojo.VipPrice;

public interface UserMapper {

	Integer userNameVerify(Map<String,String> param);
	
	void saveAddress(Address address);
	
	void updateUndefaultAddress(Integer userId);
	
	void updateAddress(Address address);
	
	void updateUserDetail(UserDetail detail);
	
	void saveUserDetail(UserDetail userDetail);
	
	void saveUser(UserInfo info);
	
	void removeAddress(Map<String,Object> param);
	
	Integer queryByWechatUnionid(String openId);
	
	void updateUserPwd(Map<String,Object> param);
	
	List<Address> listAddress(Integer userId);
	
	UserInfo getUserInfo(Integer userId);
	
	UserVip getVipUser(Map<String,Object> param);
	
	List<VipPrice> listVipPrice(Integer centerId);
	
	VipPrice getVipPrice(Integer id);
	
	void saveVipOrder(VipOrder order);
	
	UserVip getVipUserByOrderId(String orderId);
	
	void saveVipUser(UserVip userVip);
	
	void updateUserVip(UserVip userVip);
	
	void updateVipOrder(String orderId);
	
	Integer isAlreadyPay(String orderId);
	
	Integer getUserIdByUserInfo(UserInfo info);
	
	Integer countAddressByUserId(Integer userId);
	
	Integer countWechatBy3rdLogin(ThirdLogin info);
	
	void saveWechat(ThirdLogin info);
	
	void saveGrade(Grade grade);
	
	List<Integer> listCenterId();

	/**  
	 * updatePersonInChargeId:更新分级表负责人id. <br/>  
	 *  
	 * @author hebin  
	 * @param id  
	 * @since JDK 1.7  
	 */
	void updatePersonInChargeId(Grade grade);

	UserInfo getUserIdentityId(Integer userId);

	List<Grade> listGradeByParentId(Integer id);

	String getPhoneByUserId(Integer userId);
	
	Integer getParentIdByGradeId(Integer gradeId);
	
	void saveGradeData(Grade grade);
}
