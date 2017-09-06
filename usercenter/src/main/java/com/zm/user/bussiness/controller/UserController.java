package com.zm.user.bussiness.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zm.user.bussiness.service.UserService;
import com.zm.user.constants.Constants;
import com.zm.user.feignclient.ThirdPartFeignClient;
import com.zm.user.pojo.Address;
import com.zm.user.pojo.ResultModel;
import com.zm.user.pojo.UserDetail;
import com.zm.user.pojo.UserInfo;
import com.zm.user.pojo.UserVip;
import com.zm.user.pojo.VipOrder;
import com.zm.user.pojo.VipPrice;
import com.zm.user.utils.EncryptionUtil;
import com.zm.user.utils.RegularUtil;
import com.zm.user.wx.ApiResult;

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

	private static final Integer VALIDATE = 1;

	@Resource
	UserService userService;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	@Resource
	RedisTemplate<String, ApiResult> redisTemplate;

	@RequestMapping(value = "{version}/user/userNameVerify", method = RequestMethod.GET)
	public ResultModel userNameVerify(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);
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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {
			userService.saveAddress(address);
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/address/{userId}", method = RequestMethod.GET)
	public ResultModel listAddress(@PathVariable("version") Double version, HttpServletResponse res,
			@PathVariable("userId") Integer userId, HttpServletRequest req) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {
			result = userService.updateAddress(address);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/address/{userId}/{id}", method = RequestMethod.DELETE)
	public ResultModel removeAddress(@PathVariable("version") Double version, HttpServletResponse res,
			@PathVariable("userId") Integer userId, @PathVariable("id") Integer id, HttpServletRequest req) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {
			userService.updateUserDetail(detail);
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/register", method = RequestMethod.POST)
	public ResultModel registerUser(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody UserInfo info, HttpServletRequest req) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {
			info.setPhoneValidate(VALIDATE);
			if (info.getPwd() != null && !"".equals(info.getPwd())) {
				info.setPwd(EncryptionUtil.MD5(info.getPwd()));
			}

			if (info.getWechat() != null && !"".equals(info.getWechat())) {
				ApiResult apiResult = redisTemplate.opsForValue().get(info.getWechat());
				userService.packageUser(apiResult, info);
			}

			userService.saveUser(info);

			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/userDetail", method = RequestMethod.POST)
	public ResultModel saveUserDetail(@PathVariable("version") Double version, HttpServletResponse res,
			@RequestBody UserDetail detail, HttpServletRequest req) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {
			userService.saveUserDetail(detail);
			result.setSuccess(true);
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
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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

		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		String payType = req.getParameter("payType");
		String type = req.getParameter("type");
		String openId = req.getParameter("openId");

		if (payType == null || type == null) {
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
				result = userService.saveVipOrder(vipOrder, version, openId, payType, type);
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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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

		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {

			return userService.getVipUserByOrderId(orderId);

		}

		return null;
	}

	@RequestMapping(value = "{version}/user/uservip", method = RequestMethod.POST)
	public ResultModel saveUserVip(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @RequestBody UserVip userVip) {

		ResultModel result = new ResultModel();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {

			userService.updateUserVip(userVip);
			result.setSuccess(true);
		}

		return result;
	}

	@RequestMapping(value = "{version}/user/updateVipOrder/{orderId}", method = RequestMethod.PUT)
	public boolean updateVipOrder(@PathVariable("version") Double version, HttpServletResponse res,
			HttpServletRequest req, @PathVariable("orderId") String orderId) {

		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

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

}
