package com.zm.thirdcenter.bussiness.wxplugin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.bussiness.wxplugin.model.AppletCodeParameter;
import com.zm.thirdcenter.bussiness.wxplugin.service.WeiXinPluginService;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.ResultModel;
import com.zm.thirdcenter.pojo.WXLoginConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * ClassName: ThirdLoginPluginController <br/>
 * Function: 微信插件. <br/>
 * date: Aug 21, 2017 8:46:05 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@RestController
@Api(value = "第三方登录插件", description = "第三方登录插件")
public class WeiXinPluginController {

	@Resource
	WeiXinPluginService weiXinPluginService;

	@RequestMapping(value = "auth/{version}/user/3rdLogin/wx", method = RequestMethod.POST)
	@ApiOperation(value = "获取微信登录url接口", produces = "application/json;utf-8")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0") })
	public String getRequestCodeUrl(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody WXLoginConfig param) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return weiXinPluginService.getRequestCodeUrl(param);
		}
		return null;
	}

	@RequestMapping(value = "auth/{version}/user/3rdLogin/wxLogin", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel loginByWechat(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {

		if (Constants.FIRST_VERSION.equals(version)) {
			String code = req.getParameter("code");
			String state = req.getParameter("state");
			return weiXinPluginService.loginByWechat(code, state);
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "auth/{version}/user/3rdLogin/wxApplet", method = RequestMethod.GET)
	@ApiIgnore
	public ResultModel loginByWechatApplet(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestParam("code") String code, @RequestParam("userType") Integer userType,
			@RequestParam("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			return weiXinPluginService.loginByApplet(code, userType, centerId);
		}

		return new ResultModel(false, "版本错误");

	}

	@RequestMapping(value = "auth/{version}/wxshare/{centerId}", method = RequestMethod.GET)
	public ResultModel shareUrl(@PathVariable("version") Double version, @RequestParam("url") String url,
			@PathVariable("centerId") Integer centerId) {

		if (Constants.FIRST_VERSION.equals(version)) {
			// 公众号分享配置参数和公众号登录一样
			WXLoginConfig param = new WXLoginConfig();
			param.setCenterId(centerId);
			param.setLoginType(1);
			return weiXinPluginService.shareUrl(param, url);
		}
		return new ResultModel(false, "版本错误");
	}

	public ResultModel getAppletCode(@PathVariable("version") Double version, @RequestBody AppletCodeParameter param) {
		if (Constants.FIRST_VERSION.equals(version)) {
			// 获取小程序二维码
			return weiXinPluginService.getAppletCode(param);
		}
		return new ResultModel(false, "版本错误");
	}

}
