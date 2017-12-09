package com.zm.thirdcenter.bussiness.loginplugin.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.bussiness.loginplugin.util.wx.ApiResult;
import com.zm.thirdcenter.bussiness.loginplugin.util.wx.SnsAccessToken;
import com.zm.thirdcenter.bussiness.loginplugin.util.wx.SnsAccessTokenApi;
import com.zm.thirdcenter.bussiness.loginplugin.util.wx.SnsApi;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.feignclient.UserFeignClient;
import com.zm.thirdcenter.feignclient.model.ThirdLogin;
import com.zm.thirdcenter.pojo.ResultModel;
import com.zm.thirdcenter.pojo.WXLoginConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(value = "第三方登录插件", description = "第三方登录插件")
public class ThirdLoginPluginController {

	private static String authorize_uri = "https://open.weixin.qq.com/connect/oauth2/authorize";

	private static String redirect_uri = "http://api.cncoopbuy.com/3rdcenter/auth/1.0/user/3rdLogin/wxLogin";

	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Resource
	UserFeignClient userFeignClient;

	@RequestMapping(value = "auth/{version}/user/3rdLogin/wx", method = RequestMethod.POST)
	@ApiOperation(value = "获取微信登录url接口", produces = "application/json;utf-8")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public String getRequestCodeUrl(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody WXLoginConfig param) {

		if (Constants.FIRST_VERSION.equals(version)) {

			WXLoginConfig config = (WXLoginConfig) redisTemplate.opsForValue()
					.get(Constants.LOGIN + param.getCenterId() + "" + param.getLoginType());

			StringBuffer sb = new StringBuffer();
			sb.append("?appid=" + config.getAppId());
			sb.append("&redirect_uri=" + redirect_uri);
			sb.append("&response_type=code");
			if (param.isSnsapiBase()) {
				sb.append("&scope=snsapi_userinfo");
				sb.append("&state=" + param.getPlatUserType() + "_" + param.getCenterId() + "_" + param.getLoginType()
						+ "#wechat_redirect");
			} else {
				sb.append("&scope=snsapi_base");
				sb.append("&state=base" + "_" + param.getPlatUserType() + "_" + param.getCenterId() + "_"
						+ param.getLoginType() + "#wechat_redirect");
			}

			return authorize_uri + sb.toString();
		}
		return null;
	}

	@RequestMapping(value = "auth/{version}/user/3rdLogin/wxLogin", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel loginByWechat(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		ResultModel result = new ResultModel();
		if (Constants.FIRST_VERSION.equals(version)) {
			String code = req.getParameter("code");
			String state = req.getParameter("state");
			String[] stateArr = state.split("_");
			WXLoginConfig config = null;
			boolean snsapiBase = false;
			snsapiBase = Arrays.asList(stateArr).contains("base");
			if (snsapiBase) {
				config = (WXLoginConfig) redisTemplate.opsForValue()
						.get(Constants.LOGIN + stateArr[2] + "" + stateArr[3]);
			} else {
				config = (WXLoginConfig) redisTemplate.opsForValue()
						.get(Constants.LOGIN + stateArr[1] + "" + stateArr[2]);
			}
			SnsAccessToken token = SnsAccessTokenApi.getSnsAccessToken(config.getAppId(), config.getSecret(), code);

			if (!snsapiBase) {
				ApiResult apiResult = SnsApi.getUserInfo(token.getAccessToken(), token.getOpenid());

				if (apiResult.isSucceed()) {
					redisTemplate.opsForValue().set(
							apiResult.getStr("unionid") == null ? token.getOpenid() : apiResult.getStr("unionid"),
							apiResult.getJson(), 30L, TimeUnit.MINUTES);

					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("openid", token.getOpenid());
					resultMap.put("unionid", apiResult.getStr("unionid"));

					boolean flag = userFeignClient
							.get3rdLoginUser(Constants.FIRST_VERSION,
									new ThirdLogin(
											Integer.parseInt(stateArr[0]), apiResult.getStr("unionid") == null
													? token.getOpenid() : apiResult.getStr("unionid"),
							Constants.WX_LOGIN));

					resultMap.put("isFirst", flag);
					result.setSuccess(true);
					result.setObj(resultMap);
				}

			} else {
				result.setSuccess(true);
				result.setObj(token.getOpenid());
			}
		}

		return result;

	}

}
