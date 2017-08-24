package com.zm.goods.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.goods.model.TestModel;
import com.zm.goods.service.TestService;

/**
 * 
 * ClassName: Application <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Jul 27, 2017 4:53:10 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@RestController
public class TestController {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private TestService testService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Integer add(@RequestBody TestModel model) {
		Integer r = testService.add(model.getA(), model.getB());
		logger.warn("controller warn logger");
		return r;
	}
}
