package com.zm.pay.bussiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.pay.bussiness.dao.PayMapper;
import com.zm.pay.pojo.PayOriginData;
import com.zm.pay.utils.JSONUtil;
/**
 * @fun 支付中心feign接口
 * @author user
 *
 */
@RestController
public class PayFeignController {

	@Resource
	PayMapper payMapper;
	
	@RequestMapping(value="origindata/{orderId}", method = RequestMethod.GET)
	public String listPayOriginData(@PathVariable("orderId") String orderId){
		List<PayOriginData> list = payMapper.listPayOriginDataByOrderId(orderId);
		return JSONUtil.toJson(list);
	}
}
