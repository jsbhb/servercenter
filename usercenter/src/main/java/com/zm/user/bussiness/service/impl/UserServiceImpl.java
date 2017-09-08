package com.zm.user.bussiness.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.UserService;
import com.zm.user.constants.Constants;
import com.zm.user.constants.LogConstants;
import com.zm.user.feignclient.LogFeignClient;
import com.zm.user.feignclient.PayFeignClient;
import com.zm.user.feignclient.model.LogInfo;
import com.zm.user.feignclient.model.PayModel;
import com.zm.user.pojo.Address;
import com.zm.user.pojo.ResultModel;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.UserVip;
import com.zm.user.pojo.VipOrder;
import com.zm.user.pojo.VipPrice;
import com.zm.user.utils.CommonUtils;
import com.zm.user.utils.RegularUtil;
import com.zm.user.wx.ApiResult;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Integer DEFAULT = 1;

	private static final Integer ALREADY_PAY = 1;

	@Resource
	UserMapper userMapper;

	@Resource
	LogFeignClient logFeignClient;

	@Resource
	PayFeignClient payFeignClient;

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
	public void saveUser(UserInfo info) {

		userMapper.saveUser(info);

		if (info.getUserDetail() != null) {
			userMapper.saveUserDetail(info.getUserDetail());
		}

		String content = "用户通过手机号  \"" + info.getPhone() + "\"  绑定了账号";
		logFeignClient.saveLog(Constants.FIRST_VERSION,
				packageLog(LogConstants.REGISTER, "注册账号", 1, content, info.getPhone()));
	}

	@Override
	public void removeAddress(Map<String, Object> param) {

		userMapper.removeAddress(param);

	}

	@Override
	public void packageUser(ApiResult apiResult, UserInfo info) {

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
	public ResultModel saveVipOrder(VipOrder order, Double version, String openId, String payType, String type)
			throws Exception {
		ResultModel result = new ResultModel();

		String orderId = CommonUtils.getOrderId();
		order.setOrderId(orderId);

		VipPrice price = userMapper.getVipPrice(order.getVipPriceId());

		PayModel payModel = new PayModel();
		payModel.setOrderId(orderId);
		payModel.setTotalAmount((price.getPrice() * 100) + "");
		payModel.setBody("会员充值");

		if (Constants.WX_PAY.equals(payType)) {
			Map<String, String> paymap = payFeignClient.wxPay(openId, Integer.valueOf(price.getCenterId()), type,
					payModel);
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

}
