package com.zm.timetask.bussiness.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zm.timetask.constants.Constants;
import com.zm.timetask.feignclient.OrderFeignClient;
import com.zm.timetask.feignclient.SupplierFeignClient;
import com.zm.timetask.feignclient.model.OrderInfo;
import com.zm.timetask.pojo.ResultModel;
import com.zm.timetask.util.JSONUtil;

@Component
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
			List<Map<String, Object>> list = (List<Map<String, Object>>) result.getObj();
			if(list != null){
				List<OrderInfo> infoList = new ArrayList<OrderInfo>();
				OrderInfo info = null;
				for(Map<String, Object> map : list){
					redisTemplate.opsForValue().set(map.get("orderId").toString(), true);
					info = JSONUtil.parse(JSONUtil.toJson(map), OrderInfo.class);
					infoList.add(info);
				}
				supplierFeignClient.sendOrder(Constants.FIRST_VERSION, infoList);
			}
		} 
	}
	
	public static void main(String[] args) {
		String s = "{\"orderId\":\"123\",\"orderDetail\":{\"orderId\":\"123\"},\"orderGoodsList\":[{\"orderId\":\"123\"},{\"orderId\":\"123\"}]}";
		OrderInfo info = JSONUtil.parse(s, OrderInfo.class);
		System.out.println(info);
	}
}
