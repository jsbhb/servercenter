/**
 * Copyright (c) 2011-2015, Unas 小强哥 (unas@qq.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.zm.thirdcenter.wx;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.zm.thirdcenter.utils.HttpClientUtil;
import com.zm.thirdcenter.utils.RetryUtils;

/**
 *
 * 生成签名之前必须先了解一下jsapi_ticket，jsapi_ticket是公众号用于调用微信JS接口的临时票据
 * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&
 * amp;type=jsapi
 *
 * 微信卡券接口签名凭证api_ticket
 * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&
 * amp;type=wx_card
 */
public class JsTicketApi {

	private static String apiUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";

	/**
	 * JSApi的类型
	 *
	 * jsapi: 用于分享等js-api
	 *
	 * wx_card：用于卡券接口签名凭证api_ticket
	 *
	 */
	public enum JsApiType {
		jsapi, wx_card
	}

	/**
	 *
	 * http GET请求获得jsapi_ticket（有效期7200秒，利用单例来缓存）
	 *
	 * @param jsApiType
	 *            jsApi类型
	 * @return JsTicket
	 */
	public static JsTicket getTicket(JsApiType jsApiType,final String token) {
		final Map<String,String> pm = new HashMap<String, String>();
		pm.put("type", jsApiType.name());

		// 最多三次请求
		JsTicket jsTicket = RetryUtils.retryOnException(3, new Callable<JsTicket>() {

			@Override
			public JsTicket call() throws Exception {
				return new JsTicket(HttpClientUtil.get(apiUrl + token, pm));
			}

		});

		return jsTicket;
	}

}
