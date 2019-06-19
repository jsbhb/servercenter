package com.zm.user.bussiness.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zm.user.bussiness.component.UserComponent;
import com.zm.user.bussiness.dao.GradeFrontMapper;
import com.zm.user.bussiness.dao.GradeMapper;
import com.zm.user.bussiness.dao.UserMapper;
import com.zm.user.bussiness.service.GradeFrontService;
import com.zm.user.bussiness.service.UserService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.enummodel.ErrorCodeEnum;
import com.zm.user.feignclient.AuthFeignClient;
import com.zm.user.feignclient.ThirdPartFeignClient;
import com.zm.user.feignclient.model.AppletCodeParameter;
import com.zm.user.log.LogUtil;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.GradeConfig;
import com.zm.user.pojo.GradeFront;
import com.zm.user.pojo.ShopEntity;
import com.zm.user.pojo.ThirdLogin;
import com.zm.user.pojo.UserInfo;
import com.zm.user.utils.ConvertUtil;
import com.zm.user.utils.ImageUtils;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GradeFrontServiceImpl implements GradeFrontService {

	@Value("${staticUrl}")
	private String staticUrl;

	@Resource
	GradeFrontMapper gradeFrontMapper;

	@Resource
	UserMapper userMapper;

	@Resource
	UserComponent userComponent;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	@Resource
	UserService userService;

	@Resource
	AuthFeignClient authFeignClient;

	@Resource
	GradeMapper<GradeFront> gradeMapper;

	@Override
	public GradeConfig getGradeConfig(Integer mallId, Integer shopId, Integer userId) {
		if (shopId != null) {
			Integer tmallId = userComponent.getMallId(shopId);
			if (!mallId.equals(tmallId)) {
				return null;
			}
			return gradeFrontMapper.getGradeConfig(shopId);
		}
		if (userId != null) {
			UserInfo user = userMapper.getUserInfo(userId);
			if (!mallId.equals(user.getCenterId())) {
				return null;
			}
			if (user.getShopId() == null) {
				return null;
			}
			return gradeFrontMapper.getGradeConfig(user.getShopId());
		}
		return null;
	}

	@Override
	public String getClientUrl(Integer mallId) {
		return gradeFrontMapper.getClientUrlById(mallId);
	}

	@Override
	public String getMobileUrl(Integer shopId) {
		Integer parentId = userMapper.getParentIdByGradeId(shopId);

		return gradeFrontMapper.getMobileUrl(parentId);
	}

	@Override
	public byte[] getShopBillboard(Integer shopId) {
		// 获取小程序二维码流
		InputStream in = null;
		// 生成二维码
		AppletCodeParameter param = new AppletCodeParameter();
		param.setScene("shopId=" + shopId);
		param.setPage("separate/joinUs/joinUs");
		param.setWidth("400");
		param.setIs_hyaline(true);
		ResultModel model = thirdPartFeignClient.getAppletCode(Constants.FIRST_VERSION, param);
		if (!model.isSuccess()) {
			throw new RuntimeException(model.getErrorCode() + "==" + model.getErrorMsg());
		}
		// 图片字符串需base64解码
		Base64 base = new Base64();
		in = new ByteArrayInputStream(base.decode(model.getObj().toString()));
		// byte[] result = base.decode(model.getObj().toString());
		ImageUtils imageUtil = new ImageUtils();
		byte[] result = imageUtil.drawShopBillboardDTO(in, staticUrl);
		return result;
	}

	@Override
	public ResultModel applyShop(GradeFront gradeFront) {
		// 初始化
		gradeFront.init();
		String wechat = gradeFront.getWechat();
		ThirdLogin thirdLogin = new ThirdLogin();
		thirdLogin.setLoginType(Constants.WX_APPLET_LOGIN);
		thirdLogin.setThirdAccount(wechat);
		int count = userMapper.countWechatBy3rdLogin(thirdLogin);
		//设置userId
		UserInfo info = new UserInfo();
		info.setPhone(gradeFront.getPhone());
		info.setCenterId(2);
		info.setLoginType(Constants.WX_APPLET_LOGIN);
		info.setWechat(wechat);
		Integer userId = userMapper.getUserIdByUserInfo(info);
		if(userId == null){
			gradeFront.setUserId(userService.saveUser(info));
		} else {
			gradeFront.setUserId(userId);
		}
		if (count == 0) {// 第一次登陆，没注册过，后台进行注册
			userMapper.saveThirdAccount(
					new ThirdLogin(gradeFront.getUserId(), wechat, Constants.WX_APPLET_LOGIN, Constants.CONSUMER));
			com.zm.user.feignclient.model.UserInfo userInfo = new com.zm.user.feignclient.model.UserInfo();
			userInfo.setLoginType(Constants.WX_APPLET_LOGIN);
			userInfo.setOpenId(wechat);
			userInfo.setPlatUserType(Constants.CONSUMER);
			userInfo.setUserCenterId(gradeFront.getUserId());
			try {
				authFeignClient.createAuthenticationToken(userInfo);
			} catch (Exception e) {
				LogUtil.writeErrorLog("权限中心注册出错", e);
				return new ResultModel(false, ErrorCodeEnum.SERVER_ERROR.getErrorCode(),
						ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			}
		}
		GradeFront grade = gradeFrontMapper.getDataByPhone(gradeFront.getPhone());
		if(grade != null){
			return new ResultModel(false, "", "该手机号已经开通过，请用该手机号进行登录");
		}
		// 确定分级类型
		switch (gradeFront.getParentId()) {
		// 中国供销海外购下开的微店默认为门店
		case 2:
			gradeFront.setGradeType(3);// 门店
			// 其他根据父级分级的类型来确定
		default:
			getGradeType(gradeFront);
		}
		gradeFrontMapper.saveGradeFront(gradeFront);
		Grade g = ConvertUtil.converToGrade(gradeFront);
		userMapper.saveGradeData(g);
		ShopEntity sh = new ShopEntity();
		sh.setName(gradeFront.getGradeName());
		sh.setGradeId(gradeFront.getId());
		gradeMapper.insertGradeConfig(sh);
		return new ResultModel(true, null);
	}

	private void getGradeType(GradeFront gradeFront) {
		Integer tmp = gradeMapper.getGradeTypeByGradeId(gradeFront.getParentId());
		if(tmp == null){//没有该父级ID
			gradeFront.setGradeType(3);//门店
			gradeFront.setParentId(2);//父级为中国供销海外购
			return;
		}
		Integer gradeType = gradeMapper.getGradeTypeId(tmp);
		if(gradeType == null){//如果parent没有下级，自动和该parent同级
			int tmpId = gradeMapper.getParentIdById(gradeFront.getParentId());
			gradeFront.setParentId(tmpId);
			gradeFront.setGradeType(tmp);
		} else {
			gradeFront.setGradeType(gradeType);
		}
	}

	/**
	 * code 0 :未开店；1已开店，审核中；2已开店,审核失败；3已开店；4：已开店，手机号异常 ;
	 * 
	 * @param phone
	 * @return
	 */
	private final int UN_OPEN = 0;
	private final int AUDIT = 1;
	private final int AUDIT_FAIL = 2;
	private final int OPEN = 3;
	private final int EXCEPTION = 4;

	@Override
	public ResultModel applyShopCheck(String phone) {
		Integer status = gradeMapper.getGradeStatusByPhone(phone);
		int code = status == null ? UN_OPEN
				: status == 0 ? AUDIT : status == 1 ? AUDIT_FAIL : status == 2 ? OPEN : EXCEPTION;
		return new ResultModel(true, code);
	}

	@Override
	public ResultModel getDataByPhone(String phone) {
		GradeFront grade = gradeFrontMapper.getDataByPhone(phone);
		return new ResultModel(true, grade);
	}

	@Override
	public ResultModel applyShopResubmit(GradeFront gradeFront) {
		gradeFrontMapper.applyShopResubmit(gradeFront);
		gradeMapper.applyShopResubmit(gradeFront);
		return new ResultModel(true, null);
	}

}
