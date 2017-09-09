package com.zm.thirdcenter.bussiness.loginplugin.controller;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.bussiness.loginplugin.util.wx.ApiResult;
import com.zm.thirdcenter.bussiness.loginplugin.util.wx.SnsAccessToken;
import com.zm.thirdcenter.bussiness.loginplugin.util.wx.SnsAccessTokenApi;
import com.zm.thirdcenter.bussiness.loginplugin.util.wx.SnsApi;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.ResultModel;

/**
 * ClassName: ThirdLoginPluginController <br/>
 * Function: 第三方登录. <br/>
 * date: Aug 21, 2017 8:46:05 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@RestController
public class ThirdLoginPluginController {

	private static String authorize_uri = "https://open.weixin.qq.com/connect/oauth2/authorize";

	private static final String REDIRECT_URL = "http://0ca50d05.ngrok.io/auth/user/3rdLogin/wxLogin";

	@Resource
	RedisTemplate<String, ApiResult> redisTemplate;

	@RequestMapping(value = "auth/{version}/user/3rdLogin/wx", method = RequestMethod.GET)
	public String getRequestCodeUrl(boolean snsapiBase) {

		StringBuffer sb = new StringBuffer();
		sb.append("?appid=" + Constants.APPID);
		sb.append("&redirect_uri=" + REDIRECT_URL);
		sb.append("&response_type=code");
		if (snsapiBase) {
			sb.append("&scope=snsapi_userinfo");
			sb.append("&state=wx_user#wechat_redirect");
		} else {
			sb.append("&scope=snsapi_base");
			sb.append("&state=base#wechat_redirect");
		}

		return authorize_uri + sb.toString();
	}

	@RequestMapping(value = "auth/user/3rdLogin/wxLogin", method = RequestMethod.GET)
	public ResultModel loginByWechat(HttpServletRequest req, HttpServletResponse res) {

		ResultModel result = new ResultModel();

		String code = req.getParameter("code");
		String state = req.getParameter("state");
		SnsAccessToken token = SnsAccessTokenApi.getSnsAccessToken(Constants.APPID, Constants.SECRET, code);

		if ("wx_user".equals(state)) {
			ApiResult apiResult = SnsApi.getUserInfo(token.getAccessToken(), token.getOpenid());

			if (apiResult.isSucceed()) {
				redisTemplate.opsForValue().set(apiResult.getStr("unionid"), apiResult, 30L, TimeUnit.MINUTES);

				// TODO 通过权限中心API 把unionid返回给权限中心
			}

		}

		return result;

	}

}
