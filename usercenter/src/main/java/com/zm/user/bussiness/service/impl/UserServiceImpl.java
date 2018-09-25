package com.zm.user.bussiness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.user.bussiness.component.UserComponent;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.dao.WelfareMapper;
import com.zm.user.bussiness.service.UserService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.feignclient.OrderFeignClient;
import com.zm.user.feignclient.PayFeignClient;
import com.zm.user.feignclient.model.PayModel;
import com.zm.user.pojo.AbstractPayConfig;
import com.zm.user.pojo.Address;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.InviterEntity;
import com.zm.user.pojo.ThirdLogin;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.UserVip;
import com.zm.user.pojo.VipOrder;
import com.zm.user.pojo.VipPrice;
import com.zm.user.pojo.WeiXinPayConfig;
import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.utils.CommonUtils;
import com.zm.user.utils.ConvertUtil;
import com.zm.user.utils.EmojiFilter;
import com.zm.user.utils.EncryptionUtil;
import com.zm.user.utils.JSONUtil;
import com.zm.user.utils.RegularUtil;
import com.zm.user.wx.ApiResult;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserServiceImpl implements UserService {

	private static final Integer DEFAULT = 1;

	private static final Integer ALREADY_PAY = 1;

	private static final Integer VALIDATE = 1;

	@Resource
	UserMapper userMapper;

	@Resource
	OrderFeignClient orderFeignClient;

	@Resource
	PayFeignClient payFeignClient;

	@Resource
	UserComponent userComponent;

	@Resource
	GradeMapper<Grade> gradeMapper;

	@Resource
	RedisTemplate<String, String> redisTemplate;// 反序列化采用的是默认的，最初使用时微信信息都采用这种模式，所以遗留下来

	@Resource
	RedisTemplate<String, String> template;// 反序列化用的是stringSerializer，后期代码都采用该方法
	
	@Resource
	WelfareMapper welfareMapper;

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
				userMapper.saveWechat(new ThirdLogin(userId, info.getWechat(), Constants.WX_LOGIN));
			}

			info.setId(userId);
			userId = checkInviterCode(info);
			
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
			userMapper.saveWechat(new ThirdLogin(info.getId(), info.getWechat(), Constants.WX_LOGIN));
		}

		if (info.getUserDetail() != null) {
			String nickName = info.getUserDetail().getNickName();
			if (nickName != null && !"".equals(nickName)) {
				info.getUserDetail().setNickName(EmojiFilter.emojiChange(info.getUserDetail().getNickName()));
			}
			info.getUserDetail().setUserId(info.getId());
			userMapper.saveUserDetail(info.getUserDetail());
		}

		if (info.getAddress() != null) {
			info.getAddress().setUserId(info.getId());
			saveAddress(info.getAddress());
		}
		
		info.setId(checkInviterCode(info));

		return info.getId();
	}
	
	private Integer checkInviterCode(UserInfo info) {
		//校验邀请码是否正确，不正确时将user_id的值做负处理
		if (info.getInvitationCode() != null && !"".equals(info.getInvitationCode())) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("gradeId", info.getShopId());
			param.put("invitationCode", info.getInvitationCode());
			param.put("regChkStatus", 1);
			List<InviterEntity> inviterList = welfareMapper.selectInviterListByParam(param);
			if (inviterList == null || inviterList.size() <= 0) {
				info.setId(info.getId() * -1);
			}
		}
		return info.getId();
	}

	@Override
	public void removeAddress(Map<String, Object> param) {

		userMapper.removeAddress(param);

	}

	private void packageUser(ApiResult apiResult, UserInfo info) {

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
	public ResultModel saveGrade(Grade grade) {

		Map<String, Object> result = new HashMap<String, Object>();
		if (!grade.check()) {
			return new ResultModel(false, "", "对接用户请填写appKey、appSecret和对应网址");
		}
		userMapper.saveGrade(grade);// 保存完后需要主键ID
		// 前端创建文件夹
		// if (Constants.AREA_CENTER.equals(grade.getGradeType())) {
		//
		// CreateAreaCenterSEO createAreaCenterSEO = new
		// CreateAreaCenterSEO(grade.getId(), grade.getRedirectUrl(),
		// grade.getMobileUrl());
		// ResultModel temp =
		// PublishComponent.publish(JSONUtil.toJson(createAreaCenterSEO),
		// PublishType.REGION_CREATE);
		// if (temp.isSuccess()) {
		// gradeMapper.updateGradeInit(grade.getId());
		// }
		// }

		UserInfo user = new UserInfo();

		Integer mallId = userComponent.getMallId(grade.getId());
		user.setShopId(grade.getId());
		user.setCenterId(mallId);

		user.setPhone(grade.getPhone());
		Integer userId = userMapper.getUserIdByUserInfo(user);
		if (userId == null) {
			user.setPhoneValidate(VALIDATE);
			user.setStatus(1);
			userMapper.saveUser(user);
			UserDetail detail = new UserDetail();
			detail.setUserId(user.getId());
			detail.setName(grade.getPersonInCharge());
			userMapper.saveUserDetail(detail);
			userId = user.getId();
		}

		result.put("userId", userId);
		result.put("gradeId", grade.getId());

		grade.setPersonInChargeId(userId);

		if (grade.getAppKey() != null && !"".equals(grade.getAppKey())) {
			grade.setAppKey(grade.getAppKey() + "_" + grade.getId());
		}
		userMapper.updatePersonInChargeId(grade);

		// 添加注册信息存储
		userMapper.saveGradeData(grade);

		// 通知订单中心新增grade并做缓存
		GradeBO gradeBO = ConvertUtil.converToGradeBO(grade);
		template.opsForHash().put(Constants.GRADEBO_INFO, grade.getId() + "", JSONUtil.toJson(gradeBO));
		orderFeignClient.noticeToAddGrade(Constants.FIRST_VERSION, gradeBO);
		if (Constants.BUTT_JOINT_USER.equals(grade.getType())) {
			template.opsForSet().add(Constants.BUTT_JOINT_USER_PREFIX,
					JSONUtil.toJson(ConvertUtil.converToButtjoinUserBO(grade)));
		}
		return new ResultModel(true, result);
	}

	@Override
	public List<Integer> getCenterId() {

		return userMapper.listCenterId();
	}

	@Override
	public UserInfo getUserIdentityId(Integer userId) {

		return userMapper.getUserIdentityId(userId);
	}

	@Override
	public List<Grade> listGradeByParentId(Integer id) {
		return userMapper.listGradeByParentId(id);
	}

	@Override
	public String getPhoneByUserId(Integer userId) {

		return userMapper.getPhoneByUserId(userId);
	}

	@Override
	public ResultModel getAllCustomer() {
		List<UserDetail> list = userMapper.getAllCustomer();
		return new ResultModel(true, list);
	}

	@Override
	public ResultModel userBindInviterCode(UserInfo info) {
		//前端要求收到请求后返回的结果为true，校验结果放在obj字段，错误信息放在errorMsg
		ResultModel result = new ResultModel();
		result.setSuccess(true);
		
//		info.setId(checkInviterCode(info));
//		if (info.getId() < 0) {
//			result.setObj(false);
//			result.setErrorMsg("邀请码校验失败，请确认邀请码是否输入正确！");
//		} else {
//			Map<String,Object> param = new HashMap<String,Object>();
//			param.put("gradeId", info.getShopId());
//			param.put("invitationCode", info.getInvitationCode());
//			param.put("regChkStatus", 1);
//			List<InviterEntity> inviterList = welfareMapper.selectInviterListByParam(param);
//			if (inviterList == null || inviterList.size() <= 0) {
//				result.setObj(false);
//				result.setErrorMsg("邀请码校验失败，请确认邀请码是否输入正确！");
//				return result;
//			}
//			
//			InviterEntity inviter = null;
//			List<InviterEntity> updInviterList = new ArrayList<InviterEntity>();
//			for (InviterEntity ie: inviterList) {
//				if (ie.getPhone().equals(info.getPhone())) {
//					ie.setUserCenterId(info.getId());
//					ie.setStatus(3);
//					inviter = ie;
//					break;
//				}
//			}
//			if (inviter == null) {
//				inviter = inviterList.get(0);
//				inviter.setUserCenterId(info.getId());
//				inviter.setStatus(3);
//			}
//			updInviterList.add(inviter);
//			welfareMapper.updateInviterInfo(updInviterList);
//			result.setObj(true);
//		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("gradeId", info.getShopId());
		param.put("invitationCode", info.getInvitationCode());
//		param.put("regChkStatus", 1);
		List<InviterEntity> inviterList = welfareMapper.selectInviterListByParam(param);
		if (inviterList == null || inviterList.size() <= 0) {
			result.setObj(false);
			result.setErrorMsg("邀请码校验失败，请确认邀请码是否输入正确！");
			return result;
		}
		
		InviterEntity inviter = null;
		List<InviterEntity> updInviterList = new ArrayList<InviterEntity>();
		for (InviterEntity ie: inviterList) {
			if (ie.getStatus() == 3) {
				result.setObj(false);
				result.setErrorMsg("当前邀请码已被绑定，绑定的手机号为："+ie.getBindPhone());
				return result;
			} else if (ie.getStatus() == 4) {
				result.setObj(false);
				result.setErrorMsg("当前邀请码已被作废，无法绑定！");
				return result;
			}
			if (ie.getPhone().equals(info.getPhone())) {
				ie.setUserCenterId(info.getId());
				ie.setStatus(3);
				inviter = ie;
				break;
			}
		}
		if (inviter == null) {
			inviter = inviterList.get(0);
			inviter.setUserCenterId(info.getId());
			inviter.setStatus(3);
		}
		updInviterList.add(inviter);
		welfareMapper.updateInviterInfo(updInviterList);
		result.setObj(true);
		return result;
	}

	@Override
	public ResultModel userCheckInviterInfo(UserInfo info) {
		//前端要求收到请求后返回的结果为true，校验结果放在obj字段，错误信息放在errorMsg
		ResultModel result = new ResultModel();
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("gradeId", info.getShopId());
		param.put("userCenterId", info.getId());
		param.put("inviterChkStatus", 1);
		List<InviterEntity> infoList = welfareMapper.selectInviterListByParam(param);
		if (infoList != null && infoList.size() > 0) {
			result.setObj(true);
		} else {
			result.setObj(false);
			result.setErrorMsg("当前账号还不是VIP用户，请先升级！");
		}
		result.setSuccess(true);
		
		return result;
	}

}
