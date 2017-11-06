package com.zm.user.bussiness.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.UserService;
import com.zm.user.constants.Constants;
import com.zm.user.constants.LogConstants;
import com.zm.user.feignclient.GoodsFeignClient;
import com.zm.user.feignclient.LogFeignClient;
import com.zm.user.feignclient.OrderFeignClient;
import com.zm.user.feignclient.PayFeignClient;
import com.zm.user.feignclient.model.LogInfo;
import com.zm.user.feignclient.model.PayModel;
import com.zm.user.pojo.AbstractPayConfig;
import com.zm.user.pojo.Address;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ResultModel;
import com.zm.user.pojo.ThirdLogin;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.UserVip;
import com.zm.user.pojo.VipOrder;
import com.zm.user.pojo.VipPrice;
import com.zm.user.pojo.WeiXinPayConfig;
import com.zm.user.utils.CommonUtils;
import com.zm.user.utils.EmojiFilter;
import com.zm.user.utils.EncryptionUtil;
import com.zm.user.utils.RegularUtil;
import com.zm.user.wx.ApiResult;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Integer DEFAULT = 1;

	private static final Integer ALREADY_PAY = 1;

	private static final Integer VALIDATE = 1;

	@Resource
	UserMapper userMapper;

	@Resource
	LogFeignClient logFeignClient;
	
	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	PayFeignClient payFeignClient;
	
	@Resource
	GoodsFeignClient goodsFeignClient;

	@Resource
	RedisTemplate<String, String> redisTemplate;

	@Override
	public boolean userNameVerify(Map<String, String> param) {

		int count = userMapper.userNameVerify(param);
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	public void saveAddress(Address address) {

		Integer total = userMapper.countAddressByUserId(address.getUserId());

		if (total == null || total == 0) {
			address.setSetDefault(DEFAULT);
		}

		if (DEFAULT.equals(address.getSetDefault())) {
			userMapper.updateUndefaultAddress(address.getUserId());
		}

		userMapper.saveAddress(address);
	}

	@Override
	public ResultModel updateAddress(Address address) {

		ResultModel result = new ResultModel();

		if (DEFAULT.equals(address.getSetDefault())) {
			try {
				userMapper.updateUndefaultAddress(Integer.valueOf(address.getUserId()));
			} catch (NumberFormatException e) {
				result.setSuccess(false);
				result.setErrorMsg("userId参数有误");
			}
		}
		userMapper.updateAddress(address);
		result.setSuccess(true);
		return result;
	}

	@Override
	public void updateUserDetail(UserDetail detail) {

		userMapper.updateUserDetail(detail);
	}

	@Override
	public Integer saveUser(UserInfo info) {
		Integer userId = null;

		userId = userMapper.getUserIdByUserInfo(info);
		if (userId != null) {
			if (info.getWechat() != null) {
				userMapper.saveWechat(
						new ThirdLogin(userId, info.getPlatUserType(), info.getWechat(), Constants.WX_LOGIN));
			}

			return userId;
		}

		info.setPhoneValidate(VALIDATE);
		if (info.getPwd() != null && !"".equals(info.getPwd())) {
			info.setPwd(EncryptionUtil.MD5(info.getPwd()));
		}

		userMapper.saveUser(info);

		if (info.getWechat() != null && !"".equals(info.getWechat())) {
			ApiResult apiResult = new ApiResult(redisTemplate.opsForValue().get(info.getWechat()));
			packageUser(apiResult, info);
			userMapper.saveWechat(
					new ThirdLogin(info.getId(), info.getPlatUserType(), info.getWechat(), Constants.WX_LOGIN));
		}

		if (info.getUserDetail() != null) {
			info.getUserDetail().setNickName(EmojiFilter.emojiChange(info.getUserDetail().getNickName()));
			userMapper.saveUserDetail(info.getUserDetail());
		}

		String content = "用户通过手机号  \"" + info.getPhone() + "\"  绑定了账号";
		logFeignClient.saveLog(Constants.FIRST_VERSION,
				packageLog(LogConstants.REGISTER, "注册账号", info.getCenterId(), content, info.getPhone()));

		return info.getId();
	}

	@Override
	public void removeAddress(Map<String, Object> param) {

		userMapper.removeAddress(param);

	}

	private void packageUser(ApiResult apiResult, UserInfo info) {

		info.setWechat(apiResult.get("unionid") + "");
		UserDetail userDetail = new UserDetail();
		boolean isNum = RegularUtil.isNumeric(apiResult.get("sex") + "");
		if (isNum) {
			userDetail.setSex(Integer.valueOf(apiResult.get("sex") + ""));
		} else {
			userDetail.setSex(0);
		}

		userDetail
				.setLocation(apiResult.get("country") + " " + apiResult.get("province") + " " + apiResult.get("city"));
		userDetail.setHeadImg(apiResult.get("headimgurl") + "");
		userDetail.setNickName(apiResult.get("nickname") + "");
		userDetail.setUserId(info.getId());

		info.setUserDetail(userDetail);
	}

	@Override
	public void saveUserDetail(UserDetail info) {

		userMapper.saveUserDetail(info);
	}

	@Override
	public void modifyPwd(Map<String, Object> param, String phone) {

		userMapper.updateUserPwd(param);

		String content = "用户  \"" + phone + "\"  修改了密码";
		logFeignClient.saveLog(Constants.FIRST_VERSION, packageLog(LogConstants.CHANGE_PWD, "修改密码", 1, content, phone));

	}

	@Override
	public List<Address> listAddress(Integer userId) {

		return userMapper.listAddress(userId);
	}

	@Override
	public UserInfo getUserInfo(Map<String, Object> param) {

		UserInfo info = userMapper.getUserInfo((Integer) param.get("userId"));

		UserVip vipUser = userMapper.getVipUser(param);

		if (vipUser != null) {
			info.setVipLevel(vipUser.getVipLevel());
			info.setDuration(vipUser.getDuration());
			info.setVipTime(vipUser.getCreateTime());
		}

		return info;
	}

	private LogInfo packageLog(Integer apiId, String apiName, Integer clientId, String content, String opt) {
		LogInfo info = new LogInfo();

		info.setApiId(apiId);
		info.setApiName(apiName);
		info.setCenterId(LogConstants.USER_CENTER_ID);
		info.setCenterName("用户服务中心");
		info.setClientId(clientId);
		info.setContent(content);
		info.setOpt(opt);

		return info;
	}

	@Override
	public boolean getVipUser(Map<String, Object> param) {

		UserVip vipUser = userMapper.getVipUser(param);
		if (vipUser == null) {
			return false;
		}

		return true;
	}

	@Override
	public ResultModel saveVipOrder(VipOrder order, String payType, String type, AbstractPayConfig payConfig)
			throws Exception {
		ResultModel result = new ResultModel();

		String orderId = CommonUtils.getOrderId();
		order.setOrderId(orderId);

		VipPrice price = userMapper.getVipPrice(order.getVipPriceId());

		PayModel payModel = new PayModel();
		payModel.setOrderId(orderId);
		payModel.setTotalAmount((int) (price.getPrice() * 100) + "");
		payModel.setBody("会员充值");

		if (Constants.WX_PAY.equals(payType)) {
			WeiXinPayConfig weiXinPayConfig = (WeiXinPayConfig) payConfig;
			payModel.setOpenId(weiXinPayConfig.getOpenId());
			payModel.setIP(weiXinPayConfig.getIp());
			Map<String, String> paymap = payFeignClient.wxPay(Integer.valueOf(price.getCenterId()), type, payModel);
			if ("false".equals(paymap.get("success"))) {
				result.setSuccess(true);
				result.setErrorMsg("支付失败");
				return result;
			}
			result.setObj(paymap);
		} else {
			result.setSuccess(false);
			result.setErrorMsg("请指定正确的支付方式");
			return result;
		}

		userMapper.saveVipOrder(order);

		result.setSuccess(true);

		return result;
	}

	@Override
	public List<VipPrice> listVipPrice(Integer centerId) {

		return userMapper.listVipPrice(centerId);
	}

	@Override
	public UserVip getVipUserByOrderId(String orderId) {

		return userMapper.getVipUserByOrderId(orderId);
	}

	@Override
	public void saveUserVip(UserVip userVip) {

		userMapper.saveVipUser(userVip);
	}

	@Override
	public void updateUserVip(UserVip userVip) {

		userMapper.updateUserVip(userVip);
	}

	@Override
	public void updateVipOrder(String orderId) {
		userMapper.updateVipOrder(orderId);
	}

	@Override
	public boolean isAlreadyPay(String orderId) {

		Integer status = userMapper.isAlreadyPay(orderId);

		if (status.equals(ALREADY_PAY)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean verifyIsFirst(ThirdLogin info) {
		if (info.getWechat() != null) {
			Integer count = userMapper.countWechatBy3rdLogin(info);
			if (count == 0) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public Map<String,Object> saveGrade(Grade grade) {
		Map<String,Object> result = new HashMap<String, Object>();
		userMapper.saveGrade(grade);
		result.put("centerId", grade.getId());
		UserInfo user = new UserInfo();
		
		boolean flag = false;//区域中心需要新建数据表
		//生成新建等级的admin
		//设置区域中心ID,店铺ID，导购ID
		if (grade.getCenterId() == null) {//说明新建的是区域中心
			user.setPlatUserType(Constants.CENTER);
			result.put("platUserType", Constants.CENTER);
			user.setCenterId(grade.getId());
			flag = true;
		} else {
			if(grade.getShopId() == null){//说明新建的是店铺
				user.setPlatUserType(Constants.SHOP);
				result.put("platUserType", Constants.SHOP);
				user.setCenterId(grade.getCenterId());
				user.setShopId(grade.getId());
			} else {//说明新建的是导购
				user.setPlatUserType(Constants.SHOPPING_GUIDE);
				result.put("platUserType", Constants.SHOPPING_GUIDE);
				user.setCenterId(grade.getCenterId());
				user.setShopId(grade.getShopId());
				user.setGuideId(grade.getId());
			}
		}
		user.setPhone(grade.getPhone());
		user.setPhoneValidate(VALIDATE);
		user.setStatus(1);
		userMapper.saveUser(user);
		UserDetail detail = new UserDetail();
		detail.setUserId(user.getId());
		detail.setName(grade.getPersonInCharge());
		userMapper.saveUserDetail(detail);
		result.put("userId", user.getId());
		
		grade.setPersonInChargeId(user.getId());
		
		userMapper.updatePersonInChargeId(grade);
		
		if(flag){
//			goodsFeignClient.createTable(Constants.FIRST_VERSION, grade.getId());
//			orderFeignClient.createTable(Constants.FIRST_VERSION, grade.getId());
			
		}
		return result;
	}

	@Override
	public List<Integer> getCenterId() {
		
		return userMapper.listCenterId();
	}

}
