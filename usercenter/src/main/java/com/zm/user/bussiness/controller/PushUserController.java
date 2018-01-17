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

	@RequestMapping(value = "{version}/pushuser/register/{code}", method = RequestMethod.POST)
	public ResultModel pushUserRegister(@PathVariable("version") Double version, @RequestBody PushUser pushUser,
			@PathVariable("code") String code) {
		if (Constants.FIRST_VERSION.equals(version)) {
			if (!pushUser.check()) {
				return new ResultModel(false, "参数不全");
			}
			boolean flag = thirdPartFeignClient.verifyPhoneCode(Constants.FIRST_VERSION, pushUser.getPhone(), code);
			if (flag) {
				pushUserService.savePushUser(pushUser);
				return new ResultModel(true, "提交成功");
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
			return new ResultModel(true, pushUserService.pushUserAudit(pass, id));
		}

		return new ResultModel(false, "版本错误");
	}
}
