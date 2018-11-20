
package com.zm.thirdcenter.wx;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.zm.thirdcenter.bussiness.wxplugin.model.AppletCodeParameter;
import com.zm.thirdcenter.exception.WxCodeException;
import com.zm.thirdcenter.pojo.WXLoginConfig;
import com.zm.thirdcenter.utils.HttpClientUtil;
import com.zm.thirdcenter.utils.JSONUtil;
import com.zm.thirdcenter.utils.RetryUtils;

/**
 * ClassName: SnsAccessTokenApi <br/>
 * Function: 获取token. <br/>
 * date: Aug 18, 2017 4:04:44 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class SnsAccessTokenApi {
	// 授权token的url
	private static String auth_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";

	private static String applet_auth_url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";

	// 基础token的url
	private static String AccessToken_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	private static String applet_code_url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

	/**
	 * 通过code获取access_token
	 *
	 * @param code
	 *            第一步获取的code参数
	 * @param appId
	 *            应用唯一标识
	 * @param secret
	 *            应用密钥AppSecret
	 * @return SnsAccessToken
	 */
	public static SnsAccessToken getSnsAccessToken(String appId, String secret, String code) {
		final String accessTokenUrl = auth_url.replace("{appid}", appId).replace("{secret}", secret).replace("{code}",
				code == null ? "" : code);

		return RetryUtils.retryOnException(3, new Callable<SnsAccessToken>() {

			@Override
			public SnsAccessToken call() throws Exception {
				String json = HttpClientUtil.get(accessTokenUrl, null);
				return new SnsAccessToken(json);
			}
		});
	}

	/**
	 * 无条件强制更新 access token 值，不再对 cache 中的 token 进行判断
	 */
	public static SnsAccessToken refreshAccessToken(WXLoginConfig ac) {
		String appId = ac.getAppId();
		String appSecret = ac.getSecret();
		final Map<String, String> queryParas = new HashMap<String, String>();
		queryParas.put("secret", appSecret);
		queryParas.put("appid", appId);

		// 最多三次请求
		SnsAccessToken result = RetryUtils.retryOnException(3, new Callable<SnsAccessToken>() {

			@Override
			public SnsAccessToken call() throws Exception {
				String json = HttpClientUtil.get(AccessToken_url, queryParas);
				return new SnsAccessToken(json);
			}
		});

		return result;
	}

	/**
	 * 通过code获取小程序openid
	 *
	 * @param code
	 *            第一步获取的code参数
	 * @param appId
	 *            应用唯一标识
	 * @param secret
	 *            应用密钥AppSecret
	 * @return AppletSession
	 */
	public static AppletSession getAppletSession(String appId, String secret, String code) {
		final String accessTokenUrl = applet_auth_url.replace("{appid}", appId).replace("{secret}", secret)
				.replace("{code}", code == null ? "" : code);
		String json = HttpClientUtil.get(accessTokenUrl, null);
		return new AppletSession(json);
	}

	public static byte[] getAppletCode(String token, AppletCodeParameter param) throws WxCodeException {
		final String codeUrl = applet_code_url + token;
		return HttpClientUtil.postForWxCode(codeUrl, JSONUtil.toJson(param));
	}

}
