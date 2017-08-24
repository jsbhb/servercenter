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
import com.zm.thirdcenter.pojo.ResultPojo;
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

	@RequestMapping(value = "{version}/third-part/phone", method = RequestMethod.POST)
	public ResultPojo getPhoneCode(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultPojo result = new ResultPojo();
		// 设置允许跨域请求
		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		String phone = req.getParameter("phone");

		if (Constants.FIRST_VERSION.equals(version)) {

			String code = CommonUtil.getPhoneCode();

			PhoneValidata model = redisTemplate.opsForValue().get(phone);
			if (model == null) {

				boolean flag = SmsSendUtil.sendMessage(code, phone);

				if (!flag) {
					result.setSuccess(false);
					result.setErrorMsg("发送失败");
					return result;
				}

				model = new PhoneValidata();
				model.setCode(code);
				model.setSendTime(1);
				model.setTime(System.currentTimeMillis());
				redisTemplate.opsForValue().set(phone, model, 30L, TimeUnit.MINUTES);

				result.setSuccess(true);
				return result;
			} else {
				if (model.getSendTime() > 5) {
					result.setSuccess(false);
					result.setErrorMsg("30分钟内发送大于5次，请稍后再发");
					return result;
				}

				boolean flag = SmsSendUtil.sendMessage(code, phone);

				if (!flag) {
					result.setSuccess(false);
					result.setErrorMsg("发送失败");
					return result;
				}

				Long time = redisTemplate.getExpire(phone, TimeUnit.MINUTES);
				model.setCode(code);
				model.setSendTime(model.getSendTime() + 1);
				model.setTime(System.currentTimeMillis());
				redisTemplate.opsForValue().set(phone, model, time, TimeUnit.MINUTES);
				result.setSuccess(true);
				return result;
			}
		}

		return result;

	}

	@RequestMapping(value = "{version}/third-part/phoneVerify", method = RequestMethod.GET)
	public boolean verifyPhoneCode(@PathVariable("version") Double version, @RequestParam("phone") String phone,
			@RequestParam("code") String code) {

		if (Constants.FIRST_VERSION.equals(version)) {
			PhoneValidata model = redisTemplate.opsForValue().get(phone);
			if (model == null) {
				return false;
			}

			// 2分钟有效
			if (System.currentTimeMillis() - model.getTime() > EFFECTIVE_TIME) {
				return false;
			}

			code = code == null ? "" : code;
			if (code.equals(model.getCode())) {
				return true;
			}
		}

		return false;

	}
}
