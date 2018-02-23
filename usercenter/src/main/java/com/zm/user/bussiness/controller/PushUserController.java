package com.zm.user.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.user.bussiness.service.PushUserService;
import com.zm.user.common.ResultModel;
import com.zm.user.constants.Constants;
import com.zm.user.feignclient.ThirdPartFeignClient;
import com.zm.user.pojo.PushUser;

/**
 * @fun 推手控制器类
 * @author wqy
 *
 */
@RestController
public class PushUserController {

	@Resource
	PushUserService pushUserService;

	@Resource
	ThirdPartFeignClient thirdPartFeignClient;

	private static final String BACK_CODE = "erp";

	@RequestMapping(value = "{version}/pushuser/register/{code}", method = RequestMethod.POST)
	public ResultModel pushUserRegister(@PathVariable("version") Double version, @RequestBody PushUser pushUser,
			@PathVariable("code") String code) {
		if (Constants.FIRST_VERSION.equals(version)) {
			if (!pushUser.check()) {
				return new ResultModel(false, "参数不全");
			}
			boolean flag = true;
			if (!BACK_CODE.equals(code)) {
				flag = thirdPartFeignClient.verifyPhoneCode(Constants.FIRST_VERSION, pushUser.getPhone(), code);
			}
			if (flag) {
				return pushUserService.savePushUser(pushUser);
			} else {
				return new ResultModel(false, "验证码错误");
			}
		}
		return new ResultModel(false, "版本错误");
	}

	@RequestMapping(value = "{version}/pushuser/audit", method = RequestMethod.POST)
	public ResultModel pushUserAudit(@PathVariable("version") Double version, @RequestParam("pass") boolean pass,
			@RequestParam("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return pushUserService.pushUserAudit(pass, id);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 登录后获取推手绑定的店铺列表
	 * @return
	 */
	@RequestMapping(value = "{version}/listBindingGrade/{userId}", method = RequestMethod.GET)
	public ResultModel listBindingShop(@PathVariable("version") Double version,
			@PathVariable("userId") Integer userId) {
		
		if (Constants.FIRST_VERSION.equals(version)) {
			return pushUserService.listBindingShop(userId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取推手列表
	 * @return
	 */
	@RequestMapping(value = "{version}/listPushUser/{gradeId}", method = RequestMethod.GET)
	public ResultModel listPushUserByGradeId(@PathVariable("version") Double version,
			@PathVariable("gradeId") Integer gradeId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return pushUserService.listPushUserByGradeId(gradeId);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取单个推手推手
	 * @return
	 */
	@RequestMapping(value = "{version}/getPushUser/{id}", method = RequestMethod.GET)
	public ResultModel getPushUserById(@PathVariable("version") Double version, @PathVariable("id") Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return pushUserService.getPushUserById(id);
		}

		return new ResultModel(false, "版本错误");
	}

	/**
	 * @fun 获取推手是否已经在该店铺下
	 * @return
	 */
	@RequestMapping(value = "{version}/pushUserVerify/{phone}", method = RequestMethod.GET)
	public ResultModel judgePushUserIsExist(@PathVariable("version") Double version,
			@PathVariable("phone") String phone, @RequestParam("gradeId") Integer gradeId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return pushUserService.pushUserVerify(phone, gradeId);
		}

		return new ResultModel(false, "版本错误");
	}

}
