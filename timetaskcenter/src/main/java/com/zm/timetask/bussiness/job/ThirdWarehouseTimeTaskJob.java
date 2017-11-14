package com.zm.timetask.bussiness.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;
import com.zm.timetask.feignclient.SupplierFeignClient;
import com.zm.timetask.feignclient.model.OrderInfo;
import com.zm.timetask.pojo.ResultModel;

public class ThirdWarehouseTimeTaskJob {

	@Resource
	OrderFeignClient orderFeignClient;
	
	@Resource
	SupplierFeignClient supplierFeignClient;
	
	@Resource
	RedisTemplate<String, Object> redisTemplate;
	
	@SuppressWarnings("unchecked")
	public void sendOrderToWarehouse(){
		
		ResultModel result = orderFeignClient.alreadyPay(Constants.FIRST_VERSION);
		if(result.isSuccess()){
			List<OrderInfo> list = (List<OrderInfo>) result.getObj();
			for(OrderInfo info : list){
				redisTemplate.opsForValue().set(info.getOrderId(), true);
			}
			supplierFeignClient.sendOrder(Constants.FIRST_VERSION, list);
		} 
	}
}
