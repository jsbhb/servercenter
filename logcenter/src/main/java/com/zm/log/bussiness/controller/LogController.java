package com.zm.log.bussiness.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.log.bussiness.service.LogService;
import com.zm.log.constants.Constants;
import com.zm.log.pojo.LogInfo;
import com.zm.log.pojo.ResultPojo;

/**
 * ClassName: LogController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 17, 2017 3:45:14 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@RestController
public class LogController {

	@Resource
	LogService logService;

	@RequestMapping(value = "{version}/log", method = RequestMethod.POST)
	public ResultPojo saveLog(@PathVariable("version") Double version, LogInfo info, HttpServletResponse res) {

		ResultPojo result = new ResultPojo();

		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		if (Constants.FIRST_VERSION.equals(version)) {
			logService.saveLog(info);
			result.setSuccess(true);
			return result;
		}
		return result;
	}

	@RequestMapping(value = "{version}/log", method = RequestMethod.GET)
	public ResultPojo listLog(@PathVariable("version") Double version, HttpServletResponse res, HttpServletRequest req) {

		ResultPojo result = new ResultPojo();

		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		Map<String,String[]> parameter = req.getParameterMap();
		Map<String,Object> param = new HashMap<String,Object>();
		for(Map.Entry<String, String[]> entry : parameter.entrySet()){
			param.put(entry.getKey(), entry.getValue()[0]);
		}
		
		if (Constants.FIRST_VERSION.equals(version)) {
			result.setObj(logService.listLogInfo(param));
			result.setSuccess(true);
			return result;
		}
		
		return result;
	}
	
	@RequestMapping(value = "{version}/log", method = RequestMethod.DELETE)
	public ResultPojo removeLog(@PathVariable("version") Double version, HttpServletResponse res, HttpServletRequest req) {

		ResultPojo result = new ResultPojo();

		res.setHeader(Constants.CROSS_DOMAIN, Constants.DOMAIN_NAME);

		String endTime = req.getParameter("endTime");
		
		if (Constants.FIRST_VERSION.equals(version)) {
			logService.removeLogInfo(endTime);
			result.setSuccess(true);
			return result;
		}
		
		return result;
	}
}
