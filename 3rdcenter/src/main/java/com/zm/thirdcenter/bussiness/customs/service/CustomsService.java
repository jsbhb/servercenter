package com.zm.thirdcenter.bussiness.customs.service;

import com.zm.thirdcenter.bussiness.customs.model.CustomRequest;

public interface CustomsService {
	/**
	 * @fun 保存海关请求
	 * @param customs
	 * @return
	 */
	String platDataOpen(CustomRequest customs);
	
	/**
	 * @fun 返回支付信息
	 * @param customs
	 */
	void realTimeDataUp(CustomRequest customs);

}
