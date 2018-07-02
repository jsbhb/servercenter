package com.zm.user.bussiness.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zm.user.bussiness.service.UserService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.enummodel.ErrorCodeEnum;
import com.zm.user.feignclient.ThirdPartFeignClient;
import com.zm.user.log.LogUtil;
import com.zm.user.pojo.AbstractPayConfig;
import com.zm.user.pojo.Address;
import com.zm.user.pojo.Grade;
import com.zm.user.pojo.ThirdLogin;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.UserVip;
import com.zm.user.pojo.VipOrder;
import com.zm.user.pojo.VipPrice;
import com.zm.user.pojo.WeiXinPayConfig;
import com.zm.user.utils.EmojiFilter;
import com.zm.user.utils.EncryptionUtil;
import com.zm.user.utils.RegularUtil;

/**
 * ClassName: UserController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 16, 2017 3:13:00 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */

@RestController
public class UserController {

	private static final String BACK_CODE = "erp";
	
	@Resource
	UserService userService;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	@RequestMapping(value = "{version}/user/userNameVerify", method = RequestMethod.GET)
	public ResultModel userNameVerify(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();
		String account = req.getParameter("account");
		Map<String, String> param = new HashMap<String, String>();
		boolean flag = false;

		if (Constants.FIRST_VERSION.equals(version)) {
			if (Constants.EMAIL.equals(RegularUtil.emailOrPhone(account))) {
				param.put(Constants.EMAIL, account);
			}
			if (Constants.MOBILE.equals(RegularUtil.emailOrPhone(account))) {
				param.put(Constants.MOBILE, account);
			}
			if (Constants.ACCOUNT.equals(RegularUtil.emailOrPhone(account))) {
				param.put(Constants.ACCOUNT, account);
			}

			flag = userService.userNameVerify(param);

		}

		result.setSuccess(flag);
		return result;
	}

	@RequestMapping(value = "{version}/user/{centerId}/{userId}", method = RequestMethod.GET)
	public ResultModel getUserInfo(@PathVariable("version") Double version, HttpServletResponse res,
			@PathVariable("userId") Integer userId, @PathVariable("centerId") Integer centerId,
			HttpServletRequest req) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();

			param.put("userId", userId);
			param.put("centerId", centerId);
			UserInfo info = userService.getUserInfo(param);
			result.setSuccess(true);
			result.setObj(info);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/address", method = RequestMethod.POST)
	public ResultModel saveAddress(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody Address address, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			if (!address.check()) {
				result.setSuccess(false);
				result.setErrorMsg("参数不全");
				return result;
			}
			userService.saveAddress(address);
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/address/{userId}", method = RequestMethod.GET)
	public ResultModel listAddress(@PathVariable("version") Double version, HttpServletResponse res,
			@PathVariable("userId") Integer userId, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			List<Address> list = userService.listAddress(userId);
			result.setSuccess(true);
			result.setObj(list);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/address", method = RequestMethod.PUT)
	public ResultModel updateAddress(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody Address address, HttpServletRequest req) {

		ResultModel result = null;

		if (Constants.FIRST_VERSION.equals(version)) {
			result = userService.updateAddress(address);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/address/{userId}/{id}", method = RequestMethod.DELETE)
	public ResultModel removeAddress(@PathVariable("version") Double version, HttpServletResponse res,
			@PathVariable("userId") Integer userId, @PathVariable("id") Integer id, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("userId", userId);
		param.put("id", id);

		if (Constants.FIRST_VERSION.equals(version)) {
			userService.removeAddress(param);
		}

		result.setSuccess(true);
		return result;
	}

	@RequestMapping(value = "{version}/user/userDetail", method = RequestMethod.PUT)
	public ResultModel updateUserDetail(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody UserDetail detail, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			if(detail.getNickName() != null){
				detail.setNickName(EmojiFilter.emojiChange(detail.getNickName()));
			}
			userService.updateUserDetail(detail);
			result.setSuccess(true);
			result.setObj(detail);
		}

		return result;
	}

	@RequestMapping(value = "auth/{version}/user/3rdLogin-check", method = RequestMethod.POST)
	public boolean get3rdLoginUser(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody ThirdLogin info, HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			if (!info.check()) {
				throw new RuntimeException("参数不全");
			}
			return userService.verifyIsFirst(info);
		}

		return false;
	}

	@RequestMapping(value = "auth/{version}/user/register/{code}", method = RequestMethod.POST)
	public ResultModel registerUser(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody UserInfo info, @PathVariable("code") String code, HttpServletRequest req) {

		ResultModel result = new ResultModel();
		try {
			if (Constants.FIRST_VERSION.equals(version)) {

				boolean flag = false;
				if (BACK_CODE.equals(code)) {
					flag = true;
				} else {
					if (!info.check()) {
						result.setErrorMsg("参数不全");
						result.setSuccess(false);
						return result;
					}
					flag = thirdPartFeignClient.verifyPhoneCode(Constants.FIRST_VERSION, info.getPhone(), code);
				}
				if (flag) {
					Integer userId = userService.saveUser(info);
					result.setObj(userId);
					result.setSuccess(true);
				} else {
					result.setObj(Constants.PHONE_VERIFY_ERROR_CODE);
					result.setSuccess(false);
					result.setErrorMsg("验证码错误");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.writeErrorLog("用户注册", e);
			result.setErrorMsg(ErrorCodeEnum.SERVER_ERROR.getErrorMsg());
			result.setSuccess(false);
			result.setErrorCode(ErrorCodeEnum.SERVER_ERROR.getErrorCode());
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/userDetail", method = RequestMethod.POST)
	public ResultModel saveUserDetail(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody UserDetail detail, HttpServletRequest req) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {
			userService.saveUserDetail(detail);
			result.setSuccess(true);
			result.setObj(detail);
		}

		return result;
	}

	private final String localPath = "upload/headImg/";
	// 图片放后台
	private final Integer Backstage = 1;
	// 图片放阿里云OSS
	private final Integer OSS = 2;

	@RequestMapping(value = "{version}/user/{userId}/{type}/uploadHeadImg", method = RequestMethod.POST)
	public ResultModel uploadDeclFile(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("type") Integer type, @RequestParam("headImg") MultipartFile headImg, HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		ResultModel result = new ResultModel();

		if (headImg.isEmpty()) {
			result.setSuccess(false);
			result.setErrorMsg("没有文件信息");
			return result;
		}
		if (Constants.FIRST_VERSION.equals(version)) {
			String fileName = headImg.getOriginalFilename();
			if (fileName == null || (!fileName.endsWith("png") && (!fileName.endsWith("jpg")))) {
				result.setSuccess(false);
				result.setErrorMsg("请使用PNG或JPG格式");
				return result;
			}
			fileName = System.currentTimeMillis() + ".jpg";
			if (Backstage.equals(type)) {

				String newPath = localPath + userId + "/";
				String filePath = req.getSession().getServletContext().getRealPath("/") + newPath + fileName;
				File newFile = new File(filePath);
				if (!newFile.exists()) {
					newFile.mkdirs();
				}
				// 转存文件
				headImg.transferTo(new File(filePath));

				result.setSuccess(true);
				result.setObj(filePath);
			}

		}
		return result;

	}

	@RequestMapping(value = "{version}/user/modifyPwd", method = RequestMethod.PUT)
	public ResultModel modifyPwd(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();

		String code = req.getParameter("code");
		String phone = req.getParameter("phone");
		String pwd = req.getParameter("pwd");
		Integer userId = null;
		try {
			userId = Integer.valueOf(req.getParameter("userId"));
		} catch (NumberFormatException e) {
			result.setSuccess(false);
			result.setErrorMsg("用户编号参数有误");
		}
		if (Constants.FIRST_VERSION.equals(version)) {
			boolean flag = thirdPartFeignClient.verifyPhoneCode(version, phone, code);
			if (flag) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("userId", userId);
				param.put("pwd", EncryptionUtil.MD5(pwd));
				userService.modifyPwd(param, phone);

				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setErrorMsg("手机验证码错误");
			}
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/vip/{centerId}/{userId}", method = RequestMethod.GET)
	public boolean getVipUser(@PathVariable("version") Double version, HttpServletResponse res,
			@PathVariable("userId") Integer userId, @PathVariable("centerId") Integer centerId,
			HttpServletRequest req) {

		if (Constants.FIRST_VERSION.equals(version)) {
			Map<String, Object> param = new HashMap<String, Object>();

			param.put("userId", userId);
			param.put("centerId", centerId);
			return userService.getVipUser(param);
		}

		return false;
	}

	@RequestMapping(value = "{version}/user/vip-order", method = RequestMethod.POST)
	public ResultModel saveVipOrder(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @RequestBody VipOrder vipOrder) {

		ResultModel result = new ResultModel();

		String payType = req.getParameter("payType");
		String type = req.getParameter("type");
		AbstractPayConfig payConfig = null;
		if (Constants.WX_PAY.equals(payType)) {
			String openId = req.getParameter("openId");
			if (Constants.JSAPI.equals(type)) {
				if (openId == null || "".equals(openId)) {
					result.setSuccess(false);
					result.setErrorMsg("请使用微信授权登录");
					return result;
				}
			}
			String ip = req.getRemoteAddr();
			payConfig = new WeiXinPayConfig(openId, ip);
		}

		if (payType == null || type == null || "".equals(type) || "".equals(payType)) {
			result.setSuccess(false);
			result.setErrorMsg("参数不全");
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {

			if (!vipOrder.checkAttr()) {
				result.setSuccess(false);
				result.setErrorMsg("参数不全");
				return result;
			}

			try {
				result = userService.saveVipOrder(vipOrder, payType, type, payConfig);
			} catch (Exception e) {
				result.setSuccess(false);
				result.setErrorMsg("微服务出错");
				e.printStackTrace();
			}

		}

		return result;
	}

	@RequestMapping(value = "{version}/user/vip-price/{centerId}", method = RequestMethod.GET)
	public ResultModel getVipPrice(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @PathVariable("centerId") Integer centerId) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {

			List<VipPrice> list = userService.listVipPrice(centerId);
			result.setSuccess(true);
			result.setObj(list);

		}

		return result;
	}

	@RequestMapping(value = "{version}/user/getVipUser/{orderId}", method = RequestMethod.GET)
	public UserVip getVipUserByOrderId(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @PathVariable("orderId") String orderId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return userService.getVipUserByOrderId(orderId);

		}

		return null;
	}

	@RequestMapping(value = "{version}/user/uservip", method = RequestMethod.POST)
	public ResultModel saveUserVip(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @RequestBody UserVip userVip) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {

			userService.saveUserVip(userVip);
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/updateUservip", method = RequestMethod.POST)
	public ResultModel updateUserVip(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @RequestBody UserVip userVip) {

		ResultModel result = new ResultModel();

		if (Constants.FIRST_VERSION.equals(version)) {

			userService.updateUserVip(userVip);
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/updateVipOrder/{orderId}", method = RequestMethod.PUT)
	public boolean updateVipOrder(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @PathVariable("orderId") String orderId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			userService.updateVipOrder(orderId);
			return true;
		}

		return false;
	}

	@RequestMapping(value = "{version}/user/is-already-pay/{orderId}", method = RequestMethod.GET)
	public boolean isAlreadyPay(@PathVariable("version") Double version, @PathVariable("orderId") String orderId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return userService.isAlreadyPay(orderId);
		}

		return false;
	}

	@RequestMapping(value = "{version}/user/grade/save", method = RequestMethod.POST)
	public ResultModel saveGrade(@PathVariable("version") Double version, @RequestBody Grade grade) {

		if (Constants.FIRST_VERSION.equals(version)) {

			Map<String, Object> result = userService.saveGrade(grade);

			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/user/center", method = RequestMethod.GET)
	public ResultModel getCenterId(@PathVariable("version") Double version) {

		if (Constants.FIRST_VERSION.equals(version)) {

			List<Integer> result = userService.getCenterId();

			return new ResultModel(true, result);
		}

		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/user/identity/{userId}", method = RequestMethod.GET)
	public UserInfo getUserIdentityId(@PathVariable("version") Double version, @PathVariable("userId") Integer userId) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return userService.getUserIdentityId(userId);
		}

		return null;
	}

	@RequestMapping(value = "{version}/grade/{id}", method = RequestMethod.GET)
	public ResultModel getGradeNameByParentId(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {

			return new ResultModel(true, userService.listGradeByParentId(id));
		}

		return null;
	}

	@RequestMapping(value = "{version}/user/phone/{userId}", method = RequestMethod.GET)
	public String getPhoneByUserId(@PathVariable("version") Double version, @PathVariable("userId") Integer userId) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return userService.getPhoneByUserId(userId);
		}
		return null;
	}

	@RequestMapping(value = "{version}/user/customer", method = RequestMethod.POST)
	public ResultModel getAllCustomer(@PathVariable("version") Double version) {
		if (Constants.FIRST_VERSION.equals(version)) {
			return userService.getAllCustomer();
		}
		return new ResultModel(false, "版本错误");
	}

}
