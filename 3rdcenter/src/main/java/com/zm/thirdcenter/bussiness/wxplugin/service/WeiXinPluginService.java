package com.zm.thirdcenter.bussiness.wxplugin.service;

import com.zm.thirdcenter.pojo.ResultModel;
import com.zm.thirdcenter.pojo.WXLoginConfig;

public interface WeiXinPluginService {

	String getRequestCodeUrl(WXLoginConfig param);

	ResultModel loginByWechat(String code, String state);

	ResultModel shareUrl(WXLoginConfig param, String url);
}
