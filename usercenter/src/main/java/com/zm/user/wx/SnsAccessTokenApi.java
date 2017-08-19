
package com.zm.user.wx;

import java.util.concurrent.Callable;

import com.zm.user.utils.HttpClientUtil;
import com.zm.user.utils.RetryUtils;

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
	private static String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";

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
		final String accessTokenUrl = url.replace("{appid}", appId).replace("{secret}", secret).replace("{code}", code == null?"":code);

		return RetryUtils.retryOnException(3, new Callable<SnsAccessToken>() {

			@Override
			public SnsAccessToken call() throws Exception {
				String json = HttpClientUtil.get(accessTokenUrl,null);
				return new SnsAccessToken(json);
			}
		});
	}

}
