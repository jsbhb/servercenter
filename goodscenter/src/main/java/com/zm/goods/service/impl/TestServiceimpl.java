package com.zm.goods.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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
@Service("testService")
public class TestServiceimpl implements TestService {
	
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public Integer add(Integer a, Integer b) {
		Integer r = a + b;
		logger.debug("service debug logger");
		return r;
	}
}
