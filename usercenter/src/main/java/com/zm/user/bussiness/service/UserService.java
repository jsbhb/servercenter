package com.zm.user.bussiness.service;

import java.util.Map;

import com.zm.user.pojo.Address;
import com.zm.user.pojo.ResultPojo;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.wx.ApiResult;

/**  
 * ClassName: UserService <br/>  
 * Function: TODO ADD FUNCTION. <br/>   
 * date: Aug 16, 2017 4:04:48 PM <br/>  
 *  
 * @author wqy  
 * @version   
 * @since JDK 1.7  
 */
public interface UserService {

	/**  
	 * userNameVerify:验证用户名是否被注册. <br/>  
	 *  
	 * @author wqy  
	 * @param param
	 * @return  
	 * @since JDK 1.7  
	 */
	boolean userNameVerify(Map<String,String> param);
	
	/**  
	 * saveAddress:保存收货地址. <br/>  
	 *  
	 * @author wqy  
	 * @param address  
	 * @since JDK 1.7  
	 */
	void saveAddress(Address address);
	
	/**  
	 * updateAddress:修改收货地址. <br/>  
	 *  
	 * @author wqy  
	 * @param address
	 * @return  
	 * @since JDK 1.7  
	 */
	ResultPojo updateAddress(Address address);
	
	/**  
	 * removeAddress:删除收货地址. <br/>  
	 *  
	 * @author wqy  
	 * @param param  
	 * @since JDK 1.7  
	 */
	void removeAddress(Map<String,Object> param);
	
	/**  
	 * saveUserDetail:更新用户个人资料. <br/>  
	 *  
	 * @author wqy  
	 * @param detail  
	 * @since JDK 1.7  
	 */
	void updateUserDetail(UserDetail detail);
	
	/**  
	 * saveUser:注册保存用户. <br/>  
	 *  
	 * @author wqy  
	 * @param info  
	 * @since JDK 1.7  
	 */
	void saveUser(UserInfo info);
	
	/**  
	 * saveUserDetail:保存用户详细信息. <br/>  
	 *  
	 * @author wqy  
	 * @param info  
	 * @since JDK 1.7  
	 */
	void saveUserDetail(UserDetail info);
	
	
	/**  
	 * packageUser:封装用户微信信息. <br/>  
	 *  
	 * @author wqy  
	 * @param apiResult
	 * @return  
	 * @since JDK 1.7  
	 */
	void packageUser(ApiResult apiResult,UserInfo info);
	
	
	/**  
	 * modifyPwd:修改密码. <br/>  
	 *  
	 * @author wqy  
	 * @param param  
	 * @since JDK 1.7  
	 */
	void modifyPwd(Map<String,Object> param, String phone);
	
}
