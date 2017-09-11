package com.zm.thirdcenter.bussiness.phone.controller;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.PhoneValidata;
import com.zm.thirdcenter.pojo.ResultModel;
import com.zm.thirdcenter.utils.CommonUtil;
import com.zm.thirdcenter.utils.SmsSendUtil;

/**
 * ClassName: ThirdPartPhoneController <br/>
 * Function: 第三方服务-手机服务. <br/>
 * date: Aug 19, 2017 1:40:27 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@RestController
public class ThirdPartPhoneController {

	private final Long EFFECTIVE_TIME = 2 * 60 * 1000L;

	@Resource
	RedisTemplate<String, PhoneValidata> redisTemplate;

	@RequestMapping(value = "auth/{version}/third-part/phone", method = RequestMethod.POST)
	public ResultModel getPhoneCode(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();

		String phone = req.getParameter("phone");
		
		if(phone == null || "".equals(phone)){
			result.setErrorMsg("号码为空");
			result.setSuccess(false);
			return result;
		}

		if (Constants.FIRST_VERSION.equals(version)) {

			String code = CommonUtil.getPhoneCode();

			PhoneValidata model = redisTemplate.opsForValue().get(phone);
			if (model == null) {

				result = SmsSendUtil.sendMessage(code, phone);

				if (!result.isSuccess()) {
					return result;
				}

				model = new PhoneValidata();
				model.setCode(code);
				model.setSendTime(1);
				model.setTime(System.currentTimeMillis());
				redisTemplate.opsForValue().set(phone, model, 30L, TimeUnit.MINUTES);

				return result;
			} else {
				if (model.getSendTime() > 5) {
					result.setSuccess(false);
					result.setErrorMsg("30分钟内发送大于5次，请稍后再发");
					return result;
				}

				result = SmsSendUtil.sendMessage(code, phone);

				if (!result.isSuccess()) {
					return result;
				}

				Long time = redisTemplate.getExpire(phone, TimeUnit.MINUTES);
				model.setCode(code);
				model.setSendTime(model.getSendTime() + 1);
				model.setTime(System.currentTimeMillis());
				redisTemplate.opsForValue().set(phone, model, time, TimeUnit.MINUTES);
				return result;
			}
		}

		return result;

	}

	@RequestMapping(value = "auth/{version}/third-part/phoneVerify", method = RequestMethod.GET)
	public ResultModel verifyPhoneCode(@PathVariable("version") Double version, @RequestParam("phone") String phone,
			@RequestParam("code") String code) {

		ResultModel result = new ResultModel();
		
		if (Constants.FIRST_VERSION.equals(version)) {
			PhoneValidata model = redisTemplate.opsForValue().get(phone);
			if (model == null) {
				result.setSuccess(false);
				return result;
			}

			// 2分钟有效
			if (System.currentTimeMillis() - model.getTime() > EFFECTIVE_TIME) {
				result.setSuccess(false);
				return result;
			}

			code = code == null ? "" : code;
			if (code.equals(model.getCode())) {
				result.setSuccess(true);
				return result;
			}
		}

		result.setSuccess(false);
		return result;

	}
}
