package com.zm.thirdcenter.bussiness.customs.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.bussiness.customs.model.CustomRequest;
import com.zm.thirdcenter.bussiness.customs.service.CustomsService;
import com.zm.thirdcenter.utils.JSONUtil;
import com.zm.thirdcenter.utils.LogUtil;

@RestController
public class CustomsController {

	@Resource
	CustomsService customsService;
	
	@RequestMapping(value = "auth/customs/zs/platDataOpen", method = RequestMethod.POST)
	public String platDataOpen(HttpServletRequest req){
		try {
			String json = req.getParameter("openReq");
			CustomRequest customs = JSONUtil.parse(json, CustomRequest.class);
			return customsService.platDataOpen(customs);
		} catch (Exception e) {
			LogUtil.writeErrorLog("接收海关信息错误", e);
			return "{\"code\":\"20000\",\"message\":\"接收海关信息出错\",\"serviceTime\":" + System.currentTimeMillis() + "}";
		}
	}
}
