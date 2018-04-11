package com.zm.order.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.order.bussiness.service.OrderFeignService;
import com.zm.order.constants.Constants;
import com.zm.order.pojo.bo.GradeBO;

/**
 * @fun 订单控制器类，供feign调用
 * @author user
 *
 */
@RestController
public class OrderFeignController {
	
	@Resource
	OrderFeignService orderFeignService;

	/**
	 * @fun 用户中心新增grade需要通知此接口，去同步最新的grade
	 * @param version
	 */
	@RequestMapping(value = "{version}/order/feign/notice", method = RequestMethod.POST)
	public void noticeToAddGrade(@PathVariable("version") Double version, @RequestBody GradeBO grade){
		if (Constants.FIRST_VERSION.equals(version)) {
			orderFeignService.syncGrade(grade);
		}
	}
}
